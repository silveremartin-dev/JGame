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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgame.model.GameScore;
import org.jgame.server.persistence.DatabaseManager;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for user game statistics.
 *
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class UserGameStatsDAO {

    private static final Logger logger = LogManager.getLogger(UserGameStatsDAO.class);

    private final DatabaseManager dbManager;

    /**
     * Creates a new UserGameStatsDAO with injected DatabaseManager.
     * 
     * @param dbManager database manager instance
     */
    public UserGameStatsDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Gets a user's stats for a specific game type.
     *
     * @param userId user identifier (username)
     * @param gameId game type identifier
     * @return game score stats if exists
     * @throws SQLException if database error occurs
     */
    public Optional<GameScore> getStats(String userId, String gameId) throws SQLException {
        String sql = """
                SELECT u.username, s.game_type, s.total_points, s.games_played,
                       s.wins, s.losses, s.total_time_seconds, s.last_played
                FROM user_game_stats s
                JOIN users u ON s.user_id = u.id
                WHERE u.username = ? AND s.game_type = ?
                """;

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, gameId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapScore(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Gets all game stats for a user.
     *
     * @param userId user identifier
     * @return list of game scores
     * @throws SQLException if database error occurs
     */
    public List<GameScore> getAllStatsForUser(String userId) throws SQLException {
        String sql = """
                SELECT u.username, s.game_type, s.total_points, s.games_played,
                       s.wins, s.losses, s.total_time_seconds, s.last_played
                FROM user_game_stats s
                JOIN users u ON s.user_id = u.id
                WHERE u.username = ?
                ORDER BY s.total_points DESC
                """;

        List<GameScore> scores = new ArrayList<>();
        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    scores.add(mapScore(rs));
                }
            }
        }
        return scores;
    }

    /**
     * Records a game result (win or loss).
     *
     * @param score updated game score
     * @throws SQLException if database error occurs
     */
    public void saveStats(GameScore score) throws SQLException {
        String sql = """
                MERGE INTO user_game_stats (user_id, game_type, total_points, games_played,
                                            wins, losses, total_time_seconds, last_played)
                KEY (user_id, game_type)
                VALUES (
                    (SELECT id FROM users WHERE username = ?),
                    ?, ?, ?, ?, ?, ?, ?
                )
                """;

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, score.userId());
            stmt.setString(2, score.gameId());
            stmt.setLong(3, score.points());
            stmt.setInt(4, score.gamesPlayed());
            stmt.setInt(5, score.wins());
            stmt.setInt(6, score.losses());
            stmt.setLong(7, score.totalTime().toSeconds());
            stmt.setTimestamp(8, Timestamp.from(score.lastPlayed()));

            stmt.executeUpdate();
            logger.info("Saved stats for user {} on game {}: {} points, {} wins",
                    score.userId(), score.gameId(), score.points(), score.wins());
        }
    }

    /**
     * Gets the leaderboard for a game type.
     *
     * @param gameId game type identifier
     * @param limit  max number of entries
     * @return top scores sorted by points
     * @throws SQLException if database error occurs
     */
    public List<GameScore> getLeaderboard(String gameId, int limit) throws SQLException {
        String sql = """
                SELECT u.username, s.game_type, s.total_points, s.games_played,
                       s.wins, s.losses, s.total_time_seconds, s.last_played
                FROM user_game_stats s
                JOIN users u ON s.user_id = u.id
                WHERE s.game_type = ?
                ORDER BY s.total_points DESC
                LIMIT ?
                """;

        List<GameScore> scores = new ArrayList<>();
        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gameId);
            stmt.setInt(2, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    scores.add(mapScore(rs));
                }
            }
        }
        return scores;
    }

    /**
     * Gets user's rank on a game's leaderboard.
     *
     * @param userId user identifier
     * @param gameId game type identifier
     * @return rank (1-based) or 0 if not found
     * @throws SQLException if database error occurs
     */
    public int getUserRank(String userId, String gameId) throws SQLException {
        String sql = """
                SELECT COUNT(*) + 1
                FROM user_game_stats s1
                WHERE s1.game_type = ?
                AND s1.total_points > (
                    SELECT COALESCE(s2.total_points, 0)
                    FROM user_game_stats s2
                    JOIN users u ON s2.user_id = u.id
                    WHERE u.username = ? AND s2.game_type = ?
                )
                """;

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gameId);
            stmt.setString(2, userId);
            stmt.setString(3, gameId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private GameScore mapScore(ResultSet rs) throws SQLException {
        Timestamp lastPlayed = rs.getTimestamp("last_played");
        return new GameScore(
                rs.getString("username"),
                rs.getString("game_type"),
                rs.getLong("total_points"),
                rs.getInt("games_played"),
                rs.getInt("wins"),
                rs.getInt("losses"),
                Duration.ofSeconds(rs.getLong("total_time_seconds")),
                lastPlayed != null ? lastPlayed.toInstant() : Instant.now());
    }
}
