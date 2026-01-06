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
package org.jgame.server.persistence.dao;

import org.jgame.server.persistence.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Data Access Object for Score entity.
 * Handles score tracking and leaderboard queries.
 *
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class ScoreDAO {

    private static final Logger LOGGER = Logger.getLogger(ScoreDAO.class.getName());

    private final DatabaseManager dbManager;

    /**
     * Creates a new ScoreDAO with injected DatabaseManager.
     * 
     * @param dbManager database manager instance
     */
    public ScoreDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Saves a score for a user in a game.
     * 
     * @param userId   user ID
     * @param gameId   game ID
     * @param score    score value
     * @param position final position (1st, 2nd, etc.)
     * @return generated score ID, or -1 if failed
     */
    public long saveScore(long userId, long gameId, double score, int position) {
        String sql = "INSERT INTO scores (user_id, game_id, score, position) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, gameId);
            stmt.setDouble(3, score);
            stmt.setInt(4, position);

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long scoreId = rs.getLong(1);
                        LOGGER.info("Saved score for user " + userId + " in game " + gameId);
                        return scoreId;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error saving score: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Gets all scores for a specific user.
     * 
     * @param userId user ID
     * @return list of score values
     */
    public List<Double> getUserScores(long userId) {
        List<Double> scores = new ArrayList<>();
        String sql = "SELECT score FROM scores WHERE user_id = ? ORDER BY timestamp DESC";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    scores.add(rs.getDouble("score"));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error getting user scores: " + e.getMessage());
        }

        return scores;
    }

    /**
     * Gets top scores across all users.
     * 
     * @param limit maximum number of scores to return
     * @return map of username to highest score
     */
    public Map<String, Double> getTopScores(int limit) {
        Map<String, Double> topScores = new HashMap<>();
        String sql = "SELECT u.username, MAX(s.score) as max_score " +
                "FROM scores s JOIN users u ON s.user_id = u.id " +
                "GROUP BY u.username " +
                "ORDER BY max_score DESC " +
                "LIMIT ?";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    topScores.put(
                            rs.getString("username"),
                            rs.getDouble("max_score"));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error getting top scores: " + e.getMessage());
        }

        return topScores;
    }

    /**
     * Gets win count for a user (number of 1st place finishes).
     * 
     * @param userId user ID
     * @return number of wins
     */
    public int getUserWins(long userId) {
        String sql = "SELECT COUNT(*) as wins FROM scores WHERE user_id = ? AND position = 1";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("wins");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error getting user wins: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Gets average score for a user.
     * 
     * @param userId user ID
     * @return average score, or 0 if no scores
     */
    public double getUserAverageScore(long userId) {
        String sql = "SELECT AVG(score) as avg_score FROM scores WHERE user_id = ?";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("avg_score");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error getting average score: " + e.getMessage());
        }

        return 0.0;
    }
}
