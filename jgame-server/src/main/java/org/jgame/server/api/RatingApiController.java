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
import org.jgame.model.GameRating;
import org.jgame.server.persistence.dao.RatingDAO;

import java.util.List;
import java.util.Map;

/**
 * REST API controller for game ratings.
 *
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class RatingApiController {

    private static final Logger logger = LogManager.getLogger(RatingApiController.class);
    private static final Gson gson = new Gson();

    private final RatingDAO ratingDAO;

    /**
     * Creates a new RatingApiController with injected dependencies.
     * 
     * @param ratingDAO data access object for ratings
     */
    public RatingApiController(RatingDAO ratingDAO) {
        this.ratingDAO = ratingDAO;
    }

    /**
     * Default constructor for backward compatibility.
     * 
     * @deprecated Use {@link #RatingApiController(RatingDAO)} instead.
     */
    @Deprecated
    public RatingApiController() {
        this(new RatingDAO());
    }

    /**
     * GET /api/games/{gameId}/ratings - Get all ratings for a game
     */
    public void getGameRatings(Context ctx) {
        String gameId = ctx.pathParam("gameId");

        try {
            List<GameRating> ratings = ratingDAO.getRatingsForGame(gameId);
            double average = ratingDAO.getAverageRating(gameId);
            int count = ratingDAO.getRatingCount(gameId);

            ctx.json(Map.of(
                    "ratings", ratings,
                    "average", average,
                    "count", count));
        } catch (Exception e) {
            logger.error("Failed to get ratings for {}", gameId, e);
            ctx.status(500).json(Map.of("error", "Failed to get ratings"));
        }
    }

    /**
     * POST /api/ratings/{gameId} - Create a rating
     */
    public void createRating(Context ctx) {
        String gameId = ctx.pathParam("gameId");
        String username = ctx.attribute("username");

        try {
            RatingRequest req = gson.fromJson(ctx.body(), RatingRequest.class);

            if (req.stars < 1 || req.stars > 5) {
                ctx.status(400).json(Map.of("error", "Stars must be 1-5"));
                return;
            }

            GameRating rating = GameRating.create(username, gameId, req.stars, req.comment);
            ratingDAO.saveRating(rating);

            logger.info("Rating created by {} for {}: {} stars", username, gameId, req.stars);
            ctx.status(201).json(rating);

        } catch (Exception e) {
            logger.error("Failed to create rating", e);
            ctx.status(500).json(Map.of("error", "Failed to create rating"));
        }
    }

    /**
     * PUT /api/ratings/{gameId} - Update a rating
     */
    public void updateRating(Context ctx) {
        String gameId = ctx.pathParam("gameId");
        String username = ctx.attribute("username");

        try {
            RatingRequest req = gson.fromJson(ctx.body(), RatingRequest.class);

            GameRating existing = ratingDAO.getRating(username, gameId).orElse(null);
            if (existing == null) {
                ctx.status(404).json(Map.of("error", "Rating not found"));
                return;
            }

            GameRating updated = existing.withStars(req.stars).withComment(req.comment);
            ratingDAO.saveRating(updated);

            logger.info("Rating updated by {} for {}", username, gameId);
            ctx.json(updated);

        } catch (Exception e) {
            logger.error("Failed to update rating", e);
            ctx.status(500).json(Map.of("error", "Failed to update rating"));
        }
    }

    /**
     * DELETE /api/ratings/{gameId} - Delete a rating
     */
    public void deleteRating(Context ctx) {
        String gameId = ctx.pathParam("gameId");
        String username = ctx.attribute("username");

        try {
            boolean deleted = ratingDAO.deleteRating(username, gameId);

            if (deleted) {
                logger.info("Rating deleted by {} for {}", username, gameId);
                ctx.status(204);
            } else {
                ctx.status(404).json(Map.of("error", "Rating not found"));
            }

        } catch (Exception e) {
            logger.error("Failed to delete rating", e);
            ctx.status(500).json(Map.of("error", "Failed to delete rating"));
        }
    }

    private record RatingRequest(int stars, String comment) {
    }
}
