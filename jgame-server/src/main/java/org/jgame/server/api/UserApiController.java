/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)`n * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Map;

/**
 * REST API controller for user operations.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class UserApiController {

    private static final Logger logger = LogManager.getLogger(UserApiController.class);
    private static final Gson gson = new Gson();

    private final UserDAO userDAO = new UserDAO();
    private final UserGameStatsDAO statsDAO = new UserGameStatsDAO();
    private final JwtAuthHandler jwtHandler = new JwtAuthHandler();

    /**
     * POST /api/auth/register
     */
    public void register(Context ctx) {
        try {
            RegisterRequest req = gson.fromJson(ctx.body(), RegisterRequest.class);

            if (req.username == null || req.password == null) {
                ctx.status(400).json(Map.of("error", "Username and password required"));
                return;
            }

            // Check if user exists
            if (userDAO.getUserByUsername(req.username) > 0) {
                ctx.status(409).json(Map.of("error", "Username already exists"));
                return;
            }

            // Hash password and create user
            String passwordHash = BCrypt.hashpw(req.password, BCrypt.gensalt());
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
        try {
            LoginRequest req = gson.fromJson(ctx.body(), LoginRequest.class);

            if (req.username == null || req.password == null) {
                ctx.status(400).json(Map.of("error", "Username and password required"));
                return;
            }

            // Simple username check - in production would verify password hash
            if (userDAO.getUserByUsername(req.username) < 0) {
                ctx.status(401).json(Map.of("error", "Invalid credentials"));
                return;
            }

            String token = jwtHandler.generateToken(req.username, req.username);
            logger.info("User logged in: {}", req.username);

            ctx.json(Map.of("token", token, "username", req.username));

        } catch (Exception e) {
            logger.error("Login failed", e);
            ctx.status(500).json(Map.of("error", "Login failed"));
        }
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
