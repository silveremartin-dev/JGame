/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot, Google Gemini (Antigravity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jgame.server.api;

import com.google.gson.Gson;
import io.javalin.http.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgame.server.persistence.dao.UserDAO;
import org.jgame.server.persistence.dao.UserGameStatsDAO;
import org.jgame.server.auth.JwtAuthHandler;
import org.jgame.server.security.InputValidator;
import org.jgame.server.security.RateLimiter;

import java.util.List;
import java.util.Map;

/**
 * REST API controller for user operations.
 *
 * @author Silvere Martin-Michiellot
 * @version 2.1
 */
public class UserApiController {

    private static final Logger logger = LogManager.getLogger(UserApiController.class);
    private static final Gson gson = new Gson();

    private final UserDAO userDAO;
    private final UserGameStatsDAO statsDAO;
    private final JwtAuthHandler jwtHandler;
    private final RateLimiter authRateLimiter;

    /**
     * Creates a new UserApiController with injected dependencies.
     * 
     * @param userDAO    data access object for users
     * @param statsDAO   data access object for game stats
     * @param jwtHandler JWT authentication handler
     */
    public UserApiController(UserDAO userDAO, UserGameStatsDAO statsDAO, JwtAuthHandler jwtHandler) {
        this(userDAO, statsDAO, jwtHandler, RateLimiter.loginLimiter());
    }

    /**
     * Creates a new UserApiController with custom rate limiter (for testing).
     */
    public UserApiController(UserDAO userDAO, UserGameStatsDAO statsDAO, JwtAuthHandler jwtHandler, RateLimiter rateLimiter) {
        this.userDAO = userDAO;
        this.statsDAO = statsDAO;
        this.jwtHandler = jwtHandler;
        this.authRateLimiter = rateLimiter;
    }

    /**
     * POST /api/auth/register
     */
    public void register(Context ctx) {
        try {
            String clientIp = ctx.ip();
            if (authRateLimiter != null && !authRateLimiter.tryAcquire(clientIp)) {
                ctx.status(429).json(Map.of("error", "Too many requests. Please try again later."));
                return;
            }

            RegisterRequest req = gson.fromJson(ctx.body(), RegisterRequest.class);
            if (req == null) {
                ctx.status(400).json(Map.of("error", "Invalid request body"));
                return;
            }

            var userVal = InputValidator.validateUsername(req.username);
            if (!userVal.isValid()) {
                ctx.status(400).json(Map.of("error", userVal.message()));
                return;
            }

            var passVal = InputValidator.validatePassword(req.password);
            if (!passVal.isValid()) {
                ctx.status(400).json(Map.of("error", passVal.message()));
                return;
            }

            var emailVal = InputValidator.validateEmail(req.email);
            if (!emailVal.isValid()) {
                ctx.status(400).json(Map.of("error", emailVal.message()));
                return;
            }

            if (userDAO.getUserByUsername(req.username) != -1) {
                ctx.status(409).json(Map.of("error", "Username already exists"));
                return;
            }

            long userId = userDAO.createUser(req.username, req.password, req.email);

            if (userId != -1) {
                String token = jwtHandler.generateToken(String.valueOf(userId), req.username);
                ctx.status(201).json(Map.of("token", token, "username", req.username));
            } else {
                ctx.status(500).json(Map.of("error", "Failed to create user"));
            }

        } catch (Exception e) {
            logger.error("Registration error", e);
            ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }

    /**
     * POST /api/auth/login
     */
    public void login(Context ctx) {
        try {
            String clientIp = ctx.ip();
            if (authRateLimiter != null && !authRateLimiter.tryAcquire(clientIp)) {
                ctx.status(429).json(Map.of("error", "Too many login attempts. Please try again later."));
                return;
            }

            LoginRequest req = gson.fromJson(ctx.body(), LoginRequest.class);
            if (req == null || req.username == null || req.password == null) {
                ctx.status(400).json(Map.of("error", "Username and password required"));
                return;
            }

            long userId = userDAO.verifyCredentials(req.username, req.password);

            if (userId != -1) {
                String token = jwtHandler.generateToken(String.valueOf(userId), req.username);
                ctx.json(Map.of("token", token, "username", req.username));
            } else {
                ctx.status(401).json(Map.of("error", "Invalid credentials"));
            }

        } catch (Exception e) {
            logger.error("Login error", e);
            ctx.status(500).json(Map.of("error", "Internal server error"));
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
        ctx.json(Map.of("message", "Logged out successfully"));
    }

    /**
     * GET /api/user/profile
     */
    public void getProfile(Context ctx) {
        String username = ctx.attribute("username");
        String userId = ctx.attribute("userId");
        ctx.json(Map.of(
                "userId", userId != null ? userId : "",
                "username", username != null ? username : "",
                "role", "user"));
    }

    /**
     * PUT /api/user/profile
     */
    public void updateProfile(Context ctx) {
        String username = ctx.attribute("username");
        if (username == null) {
            ctx.status(401).json(Map.of("error", "Unauthorized"));
            return;
        }

        UpdateProfileRequest req = gson.fromJson(ctx.body(), UpdateProfileRequest.class);
        if (req == null) {
            ctx.status(400).json(Map.of("error", "Invalid request body"));
            return;
        }

        if (req.email != null && !req.email.isBlank()) {
            var emailVal = InputValidator.validateEmail(req.email);
            if (!emailVal.isValid()) {
                ctx.status(400).json(Map.of("error", emailVal.message()));
                return;
            }
        }

        if (req.password != null && !req.password.isBlank()) {
            var passVal = InputValidator.validatePassword(req.password);
            if (!passVal.isValid()) {
                ctx.status(400).json(Map.of("error", passVal.message()));
                return;
            }
        }

        if (userDAO.updateUser(username, req.email, req.password)) {
            ctx.json(Map.of("message", "Profile updated successfully"));
        } else {
            ctx.status(400).json(Map.of("error", "Failed to update profile or nothing to update"));
        }
    }

    /**
     * GET /api/user/scores
     */
    public void getScores(Context ctx) {
        String username = ctx.attribute("username");
        if (username == null) {
            ctx.status(401).json(Map.of("error", "Unauthorized"));
            return;
        }

        try {
            List<?> scores = statsDAO.getAllStatsForUser(username);
            ctx.json(scores);
        } catch (Exception e) {
            logger.error("Error getting scores for user {}", username, e);
            ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }

    private record RegisterRequest(String username, String password, String email) {
    }

    private record LoginRequest(String username, String password) {
    }

    private record UpdateProfileRequest(String email, String password) {
    }
}
