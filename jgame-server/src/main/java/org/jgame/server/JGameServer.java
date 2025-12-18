/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.server;

import io.javalin.Javalin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgame.persistence.DatabaseManager;
import org.jgame.server.api.GameApiController;
import org.jgame.server.api.RatingApiController;
import org.jgame.server.api.UserApiController;
import org.jgame.server.auth.JwtAuthHandler;

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
 * @version 1.0
 */
public class JGameServer {

    private static final Logger logger = LogManager.getLogger(JGameServer.class);
    private static final int DEFAULT_PORT = 8080;

    private final Javalin app;
    private final int port;

    /**
     * Creates a new game server.
     *
     * @param port server port
     */
    public JGameServer(int port) {
        this.port = port;
        this.app = createApp();
    }

    private Javalin createApp() {
        return Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.bundledPlugins.enableCors(cors -> cors.addRule(it -> {
                it.allowHost("*");
            }));
        });
    }

    /**
     * Starts the server.
     */
    public void start() {
        // Initialize database
        try {
            DatabaseManager.initialize();
            logger.info("Database initialized");
        } catch (Exception e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }

        // Register routes
        registerRoutes();

        // Start server
        app.start(port);
        logger.info("JGame Server started on port {}", port);
    }

    private void registerRoutes() {
        JwtAuthHandler authHandler = new JwtAuthHandler();
        UserApiController userController = new UserApiController();
        GameApiController gameController = new GameApiController();
        RatingApiController ratingController = new RatingApiController();

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

        logger.info("Routes registered");
    }

    /**
     * Stops the server.
     */
    public void stop() {
        app.stop();
        DatabaseManager.shutdown();
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
