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

import java.util.List;
import java.util.Map;

/**
 * REST API controller for user operations.
 *
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class UserApiController {

    private static final Logger logger = LogManager.getLogger(UserApiController.class);
    private static final Gson gson = new Gson();

    private final UserDAO userDAO;
    private final UserGameStatsDAO statsDAO;
    private final JwtAuthHandler jwtHandler;

    /**
     * Creates a new UserApiController with injected dependencies.
     * 
     * @param userDAO    data access object for users
     * @param statsDAO   data access object for game stats
     * @param jwtHandler JWT authentication handler
     */
    public UserApiController(UserDAO userDAO, UserGameStatsDAO statsDAO, JwtAuthHandler jwtHandler) {
        this.userDAO = userDAO;
        this.statsDAO = statsDAO;
        this.jwtHandler = jwtHandler;
    }

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

            if (userDAO.getUserByUsername(req.username) != -1) {
                ctx.status(409).json(Map.of("error", "Username already exists"));
                return;
            }

            // In a real app, hash the password!
            // For now, assuming raw password for demo (BAD PRACTICE, fix in security
            // update)
            long userId = userDAO.createUser(req.username, req.password, req.email);

            if (userId != -1) {
                String token = jwtHandler.generateToken(req.username, "user");
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
            LoginRequest req = gson.fromJson(ctx.body(), LoginRequest.class);

            long userId = userDAO.verifyCredentials(req.username, req.password);

            if (userId != -1) {
                String token = jwtHandler.generateToken(req.username, "user");
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
     * GET /api/users/me
     */
    public void getProfile(Context ctx) {
        String username = ctx.attribute("username");
        // Simplified profile response
        ctx.json(Map.of("username", username, "role", "user"));
    }

    /**
     * PUT /api/user/profile
     */
    public void updateProfile(Context ctx) {
        String username = ctx.attribute("username");
        UpdateProfileRequest req = gson.fromJson(ctx.body(), UpdateProfileRequest.class);

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
        try {
            List<?> scores = statsDAO.getAllStatsForUser(username);
            ctx.json(scores);
        } catch (Exception e) {
            logger.error("Error getting scores", e);
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
