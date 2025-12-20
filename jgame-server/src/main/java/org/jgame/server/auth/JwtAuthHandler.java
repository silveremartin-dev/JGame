/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.server.auth;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgame.server.security.TokenBlacklist;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

/**
 * JWT authentication handler for protected routes.
 * 
 * <p>
 * Loads secret key from environment variable JGAME_JWT_SECRET,
 * falling back to application.properties if not set.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @version 2.1
 */
public class JwtAuthHandler implements Handler {

    private static final Logger logger = LogManager.getLogger(JwtAuthHandler.class);
    private static final String DEFAULT_SECRET = "jgame-dev-secret-key-change-in-production-256bit";
    private static final long DEFAULT_EXPIRATION_MS = 24 * 60 * 60 * 1000; // 24 hours

    private final SecretKey key;
    private final long expirationMs;

    public JwtAuthHandler() {
        String secret = loadSecret();
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = loadExpirationMs();
        logger.info("JWT handler initialized (secret from {})",
                System.getenv("JGAME_JWT_SECRET") != null ? "environment" : "config/default");
    }

    private String loadSecret() {
        // Priority 1: Environment variable
        String envSecret = System.getenv("JGAME_JWT_SECRET");
        if (envSecret != null && !envSecret.isBlank()) {
            return envSecret;
        }

        // Priority 2: application.properties
        Properties props = loadProperties();
        String propSecret = props.getProperty("jwt.secret");
        if (propSecret != null && !propSecret.isBlank() && !propSecret.startsWith("${")) {
            return propSecret;
        }

        // Priority 3: Default (dev only)
        logger.warn("Using default JWT secret - NOT SAFE FOR PRODUCTION");
        return DEFAULT_SECRET;
    }

    private long loadExpirationMs() {
        Properties props = loadProperties();
        String hours = props.getProperty("jwt.expiration.hours", "24");
        try {
            return Long.parseLong(hours) * 60 * 60 * 1000;
        } catch (NumberFormatException e) {
            return DEFAULT_EXPIRATION_MS;
        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/application.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (IOException e) {
            logger.warn("Could not load application.properties");
        }
        return props;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        String authHeader = ctx.header("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header");
            throw new UnauthorizedResponse("Missing authentication token");
        }

        String token = authHeader.substring(7);

        // Check blacklist
        if (TokenBlacklist.getInstance().isRevoked(token)) {
            logger.warn("Revoked token used");
            throw new UnauthorizedResponse("Token has been revoked");
        }

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Store user info in context for later use
            ctx.attribute("userId", claims.getSubject());
            ctx.attribute("username", claims.get("username", String.class));

        } catch (Exception e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
            throw new UnauthorizedResponse("Invalid authentication token");
        }
    }

    /**
     * Generates a JWT token for a user.
     *
     * @param userId   user ID
     * @param username username
     * @return JWT token
     */
    public String generateToken(String userId, String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userId)
                .claim("username", username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    /**
     * Revokes a token.
     * 
     * @param token the token string (without Bearer prefix)
     */
    public void revokeToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Instant expiry = claims.getExpiration().toInstant();
            TokenBlacklist.getInstance().revoke(token, expiry);
        } catch (Exception e) {
            logger.warn("Failed to revoke token: {}", e.getMessage());
        }
    }
}
