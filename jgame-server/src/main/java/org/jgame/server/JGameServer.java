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
package org.jgame.server;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgame.server.persistence.DatabaseManager;
import org.jgame.server.persistence.dao.RatingDAO;
import org.jgame.server.persistence.dao.UserDAO;
import org.jgame.server.persistence.dao.UserGameStatsDAO;
import org.jgame.server.api.GameApiController;
import org.jgame.server.api.RatingApiController;
import org.jgame.server.api.UserApiController;
import org.jgame.server.auth.JwtAuthHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Main headless game server.
 * 
 * <p>
 * Provides REST API for:
 * </p>
 * <ul>
 * <li>User registration and authentication</li>
 * <li>Game discovery and metadata</li>
 * <li>Ratings and reviews</li>
 * <li>Leaderboards and scores</li>
 * <li>Lobby system (future)</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class JGameServer {

    private static final Logger logger = LogManager.getLogger(JGameServer.class);
    private static final int DEFAULT_PORT = 8080;

    private final Javalin app;
    private final int port;
    private final DatabaseManager dbManager;

    /**
     * Creates a new game server.
     *
     * @param port server port
     */
    public JGameServer(int port) {
        this.port = port;
        this.app = createApp();
        this.dbManager = DatabaseManager.getInstance();
    }

    private Javalin createApp() {
        List<String> allowedOrigins = loadAllowedOrigins();

        Javalin javalin = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";

            // CORS: Restrict to configured origins (security hardening)
            config.bundledPlugins.enableCors(cors -> cors.addRule(it -> {
                if (allowedOrigins.contains("*")) {
                    logger.warn("CORS allowing all origins - NOT SAFE FOR PRODUCTION");
                    it.allowHost("*");
                } else {
                    for (String origin : allowedOrigins) {
                        it.allowHost(origin);
                    }
                    logger.info("CORS restricted to: {}", allowedOrigins);
                }
            }));
        });

        // Global exception handler for JSON error responses
        configureExceptionHandlers(javalin);

        return javalin;
    }

    /**
     * Configures global exception handlers for standardized JSON error responses.
     */
    private void configureExceptionHandlers(Javalin app) {
        // Handle generic exceptions
        app.exception(Exception.class, (e, ctx) -> {
            logger.error("Unhandled exception: {}", e.getMessage(), e);
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(Map.of(
                    "error", "Internal Server Error",
                    "message", "An unexpected error occurred",
                    "code", 500));
        });

        // Handle illegal argument exceptions
        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            logger.warn("Bad request: {}", e.getMessage());
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(Map.of(
                    "error", "Bad Request",
                    "message", e.getMessage(),
                    "code", 400));
        });

        // Handle not found scenarios
        app.error(404, ctx -> {
            ctx.json(Map.of(
                    "error", "Not Found",
                    "message", "The requested resource was not found",
                    "code", 404));
        });

        logger.info("Global exception handlers configured");
    }

    /**
     * Loads allowed CORS origins from configuration.
     */
    private List<String> loadAllowedOrigins() {
        // Priority 1: Environment variable
        String envOrigins = System.getenv("JGAME_CORS_ORIGINS");
        if (envOrigins != null && !envOrigins.isBlank()) {
            return Arrays.asList(envOrigins.split(","));
        }

        // Priority 2: application.properties
        Properties props = loadProperties();
        String propOrigins = props.getProperty("cors.allowed.origins");
        if (propOrigins != null && !propOrigins.isBlank()) {
            return Arrays.asList(propOrigins.split(","));
        }

        // Default: localhost only (secure default)
        return List.of("http://localhost:3000", "http://localhost:8080");
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

    /**
     * Starts the server.
     */
    public void start() {
        // Initialize database
        try {
            dbManager.initialize();
            logger.info("Database initialized");
        } catch (Exception e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }

        // Register routes with dependency injection
        registerRoutes();

        // Start server
        app.start(port);
        logger.info("JGame Server started on port {}", port);
    }

    private void registerRoutes() {
        // Composition Root: Instantiate and wire dependencies

        // Data Access Layer
        UserDAO userDAO = new UserDAO(dbManager);
        UserGameStatsDAO statsDAO = new UserGameStatsDAO(dbManager);
        RatingDAO ratingDAO = new RatingDAO(dbManager);

        // Security/Auth
        JwtAuthHandler authHandler = new JwtAuthHandler();

        // Controllers (Presentation Layer)
        UserApiController userController = new UserApiController(userDAO, statsDAO, authHandler);
        GameApiController gameController = new GameApiController(statsDAO);
        RatingApiController ratingController = new RatingApiController(ratingDAO);

        // Public routes
        app.post("/api/auth/register", userController::register);
        app.post("/api/auth/login", userController::login);
        app.get("/api/games", gameController::listGames);
        app.get("/api/games/{gameId}", gameController::getGame);
        app.get("/api/games/{gameId}/ratings", ratingController::getGameRatings);

        // Protected routes (require JWT)
        app.before("/api/user/*", authHandler);
        app.before("/api/ratings/*", authHandler);
        app.before("/api/scores/*", authHandler);

        // User routes
        app.get("/api/user/profile", userController::getProfile);
        app.put("/api/user/profile", userController::updateProfile);
        app.get("/api/user/scores", userController::getScores);

        // Rating routes
        app.post("/api/ratings/{gameId}", ratingController::createRating);
        app.put("/api/ratings/{gameId}", ratingController::updateRating);
        app.delete("/api/ratings/{gameId}", ratingController::deleteRating);

        // Score routes
        app.get("/api/scores/{gameId}/leaderboard", gameController::getLeaderboard);

        // Health check
        app.get("/health", ctx -> ctx.result("OK"));

        logger.info("Routes registered with injected dependencies");
    }

    /**
     * Stops the server.
     */
    public void stop() {
        app.stop();
        dbManager.shutdown();
        logger.info("JGame Server stopped");
    }

    /**
     * Gets the Javalin app instance (for testing).
     */
    public Javalin getApp() {
        return app;
    }

    /**
     * Main entry point.
     */
    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        // Parse command line args
        for (int i = 0; i < args.length; i++) {
            if ("-p".equals(args[i]) || "--port".equals(args[i])) {
                if (i + 1 < args.length) {
                    port = Integer.parseInt(args[++i]);
                }
            }
        }

        JGameServer server = new JGameServer(port);

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down...");
            server.stop();
        }));

        server.start();
    }
}
