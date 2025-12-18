/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)`n * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT authentication handler for protected routes.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class JwtAuthHandler implements Handler {

    private static final Logger logger = LogManager.getLogger(JwtAuthHandler.class);

    // In production, use environment variable
    private static final String SECRET_KEY = "jgame-super-secret-key-that-is-at-least-256-bits-long";
    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000; // 24 hours

    private final SecretKey key;

    public JwtAuthHandler() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void handle(Context ctx) throws Exception {
        String authHeader = ctx.header("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header");
            throw new UnauthorizedResponse("Missing authentication token");
        }

        String token = authHeader.substring(7);

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
        Date expiry = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .subject(userId)
                .claim("username", username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }
}
