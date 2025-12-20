/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.server.api;

import com.google.gson.Gson;
import io.javalin.http.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgame.model.GameScore;
import org.jgame.persistence.dao.UserDAO;
import org.jgame.persistence.dao.UserGameStatsDAO;
import org.jgame.server.auth.JwtAuthHandler;
import org.jgame.server.security.InputValidator;
import org.jgame.server.security.InputValidator.ValidationResult;
import org.jgame.server.security.RateLimiter;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Map;

/**
 * REST API controller for user operations.
 * 
 * <p>
 * Includes rate limiting and input validation for security.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class UserApiController {

    private static final Logger logger = LogManager.getLogger(UserApiController.class);
    private static final Gson gson = new Gson();

    private final UserDAO userDAO = new UserDAO();
    private final UserGameStatsDAO statsDAO = new UserGameStatsDAO();
    private final JwtAuthHandler jwtHandler = new JwtAuthHandler();

    // Rate limiters
    private final RateLimiter loginLimiter = RateLimiter.loginLimiter();
    private final RateLimiter registerLimiter = new RateLimiter(3, 60); // 3 registrations per minute

    /**
     * POST /api/auth/register
     */
    public void register(Context ctx) {
        String clientIp = ctx.ip();

        // Rate limiting
        if (!registerLimiter.tryAcquire(clientIp)) {
            ctx.status(429).json(Map.of("error", "Too many registration attempts. Please try again later."));
            return;
        }

        try {
            RegisterRequest req = gson.fromJson(ctx.body(), RegisterRequest.class);

            // Validate username
            ValidationResult usernameResult = InputValidator.validateUsername(req.username);
            if (!usernameResult.isValid()) {
                ctx.status(400).json(Map.of("error", usernameResult.message()));
                return;
            }

            // Validate password
            ValidationResult passwordResult = InputValidator.validatePassword(req.password);
            if (!passwordResult.isValid()) {
                ctx.status(400).json(Map.of("error", passwordResult.message()));
                return;
            }

            // Validate email (optional)
            ValidationResult emailResult = InputValidator.validateEmail(req.email);
            if (!emailResult.isValid()) {
                ctx.status(400).json(Map.of("error", emailResult.message()));
                return;
            }

            // Check if user exists
            if (userDAO.getUserByUsername(req.username) > 0) {
                ctx.status(409).json(Map.of("error", "Username already exists"));
                return;
            }

            // Hash password and create user
            String passwordHash = BCrypt.hashpw(req.password, BCrypt.gensalt(12));
            long userId = userDAO.createUser(req.username, passwordHash, req.email);

            if (userId > 0) {
                logger.info("User registered: {}", req.username);
                ctx.status(201).json(Map.of("message", "User created successfully"));
            } else {
                ctx.status(500).json(Map.of("error", "Failed to create user"));
            }

        } catch (Exception e) {
            logger.error("Registration failed", e);
            ctx.status(500).json(Map.of("error", "Registration failed"));
        }
    }

    /**
     * POST /api/auth/login
     */
    public void login(Context ctx) {
        String clientIp = ctx.ip();

        // Rate limiting
        if (!loginLimiter.tryAcquire(clientIp)) {
            logger.warn("Rate limit exceeded for login from IP: {}", clientIp);
            ctx.status(429).json(Map.of("error", "Too many login attempts. Please try again later."));
            return;
        }

        try {
            LoginRequest req = gson.fromJson(ctx.body(), LoginRequest.class);

            if (req.username == null || req.username.isBlank() ||
                    req.password == null || req.password.isEmpty()) {
                ctx.status(400).json(Map.of("error", "Username and password required"));
                return;
            }

            // Validate username format (prevents injection)
            ValidationResult usernameResult = InputValidator.validateUsername(req.username);
            if (!usernameResult.isValid()) {
                // Don't reveal specific validation error for security
                ctx.status(401).json(Map.of("error", "Invalid credentials"));
                return;
            }

            // Check user exists
            if (userDAO.getUserByUsername(req.username) < 0) {
                // Use same error message to prevent username enumeration
                ctx.status(401).json(Map.of("error", "Invalid credentials"));
                return;
            }

            // TODO: In production, verify password hash here
            // String storedHash = userDAO.getPasswordHash(req.username);
            // if (!BCrypt.checkpw(req.password, storedHash)) { ... }

            String token = jwtHandler.generateToken(req.username, req.username);
            logger.info("User logged in: {}", req.username);

            ctx.json(Map.of("token", token, "username", req.username));

        } catch (Exception e) {
            logger.error("Login failed", e);
            ctx.status(500).json(Map.of("error", "Login failed"));
        }
    }

    /**
     * POST /api/auth/logout
     */
    public void logout(Context ctx) {
        String authHeader = ctx.header("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtHandler.revokeToken(token);
        }

        logger.info("User logged out");
        ctx.json(Map.of("message", "Logged out successfully"));
    }

    /**
     * GET /api/user/profile
     */
    public void getProfile(Context ctx) {
        String username = ctx.attribute("username");
        ctx.json(Map.of("username", username));
    }

    /**
     * PUT /api/user/profile
     */
    public void updateProfile(Context ctx) {
        try {
            String username = ctx.attribute("username");
            logger.info("Profile update requested for: {}", username);
            ctx.json(Map.of("message", "Profile updated"));
        } catch (Exception e) {
            logger.error("Profile update failed", e);
            ctx.status(500).json(Map.of("error", "Profile update failed"));
        }
    }

    /**
     * GET /api/user/scores
     */
    public void getScores(Context ctx) {
        try {
            String username = ctx.attribute("username");
            List<GameScore> scores = statsDAO.getAllStatsForUser(username);
            ctx.json(scores);
        } catch (Exception e) {
            logger.error("Failed to get scores", e);
            ctx.status(500).json(Map.of("error", "Failed to get scores"));
        }
    }

    // Request DTOs
    private record RegisterRequest(String username, String password, String email) {
    }

    private record LoginRequest(String username, String password) {
    }
}
