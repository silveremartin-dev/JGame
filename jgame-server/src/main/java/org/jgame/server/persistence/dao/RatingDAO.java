/*
 * MIT License
 *
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.server.persistence.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgame.model.GameRating;
import org.jgame.server.persistence.DatabaseManager;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for game ratings.
 *
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class RatingDAO {

    private static final Logger logger = LogManager.getLogger(RatingDAO.class);

    private final DatabaseManager dbManager;

    /**
     * Creates a new RatingDAO with injected DatabaseManager.
     * 
     * @param dbManager database manager instance
     */
    public RatingDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Default constructor for backward compatibility.
     * Uses the singleton DatabaseManager instance.
     * 
     * @deprecated Use {@link #RatingDAO(DatabaseManager)} instead.
     */
    @Deprecated
    public RatingDAO() {
        this(DatabaseManager.getInstance());
    }

    /**
     * Creates or updates a rating.
     *
     * @param rating the rating to save
     * @return saved rating with ID
     * @throws SQLException if database error occurs
     */
    public GameRating saveRating(GameRating rating) throws SQLException {
        String sql = """
                MERGE INTO ratings (user_id, game_type, stars, comment, created_at, updated_at)
                KEY (user_id, game_type)
                VALUES (
                    (SELECT id FROM users WHERE username = ?),
                    ?, ?, ?, ?, ?
                )
                """;

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rating.userId());
            stmt.setString(2, rating.gameId());
            stmt.setInt(3, rating.stars());
            stmt.setString(4, rating.comment());
            stmt.setTimestamp(5, Timestamp.from(rating.createdAt()));
            stmt.setTimestamp(6, Timestamp.from(Instant.now()));

            stmt.executeUpdate();
            logger.info("Saved rating for user {} on game {}: {} stars",
                    rating.userId(), rating.gameId(), rating.stars());

            return rating;
        }
    }

    /**
     * Gets a user's rating for a game.
     *
     * @param userId user identifier (username)
     * @param gameId game type identifier
     * @return rating if exists
     * @throws SQLException if database error occurs
     */
    public Optional<GameRating> getRating(String userId, String gameId) throws SQLException {
        String sql = """
                SELECT u.username, r.game_type, r.stars, r.comment, r.created_at, r.updated_at
                FROM ratings r
                JOIN users u ON r.user_id = u.id
                WHERE u.username = ? AND r.game_type = ?
                """;

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, gameId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRating(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Gets all ratings for a game.
     *
     * @param gameId game type identifier
     * @return list of ratings
     * @throws SQLException if database error occurs
     */
    public List<GameRating> getRatingsForGame(String gameId) throws SQLException {
        String sql = """
                SELECT u.username, r.game_type, r.stars, r.comment, r.created_at, r.updated_at
                FROM ratings r
                JOIN users u ON r.user_id = u.id
                WHERE r.game_type = ?
                ORDER BY r.updated_at DESC
                """;

        List<GameRating> ratings = new ArrayList<>();
        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gameId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ratings.add(mapRating(rs));
                }
            }
        }
        return ratings;
    }

    /**
     * Gets average rating for a game.
     *
     * @param gameId game type identifier
     * @return average stars (0 if no ratings)
     * @throws SQLException if database error occurs
     */
    public double getAverageRating(String gameId) throws SQLException {
        String sql = "SELECT AVG(stars) FROM ratings WHERE game_type = ?";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gameId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    /**
     * Gets rating count for a game.
     *
     * @param gameId game type identifier
     * @return number of ratings
     * @throws SQLException if database error occurs
     */
    public int getRatingCount(String gameId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ratings WHERE game_type = ?";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gameId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Deletes a rating.
     *
     * @param userId user identifier
     * @param gameId game type identifier
     * @return true if deleted
     * @throws SQLException if database error occurs
     */
    public boolean deleteRating(String userId, String gameId) throws SQLException {
        String sql = """
                DELETE FROM ratings
                WHERE user_id = (SELECT id FROM users WHERE username = ?)
                AND game_type = ?
                """;

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, gameId);

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                logger.info("Deleted rating for user {} on game {}", userId, gameId);
                return true;
            }
        }
        return false;
    }

    private GameRating mapRating(ResultSet rs) throws SQLException {
        return new GameRating(
                rs.getString("username"),
                rs.getString("game_type"),
                rs.getInt("stars"),
                rs.getString("comment"),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant());
    }
}
