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
import java.util.List;
import java.util.logging.Logger;

/**
 * Data Access Object for Game entity.
 * Handles saving/loading game states using JSON serialization.
 *
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class GameDAO {

    private static final Logger LOGGER = Logger.getLogger(GameDAO.class.getName());

    private final DatabaseManager dbManager;

    /**
     * Creates a new GameDAO with injected DatabaseManager.
     * 
     * @param dbManager database manager instance
     */
    public GameDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Saves a new game to the database.
     * 
     * @param gameType  type of game (e.g., "GOOSE", "CHECKERS", "CHESS")
     * @param stateJson serialized game state as JSON
     * @return generated game ID, or -1 if failed
     */
    public long saveGame(String gameType, String stateJson) {
        String sql = "INSERT INTO games (game_type, state_json, last_played) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, gameType);
            stmt.setString(2, stateJson);

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long gameId = rs.getLong(1);
                        LOGGER.info("Saved game: " + gameType + " (ID: " + gameId + ")");
                        return gameId;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error saving game: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Updates an existing game's state.
     * 
     * @param gameId    game ID
     * @param stateJson new state JSON
     * @return true if updated, false otherwise
     */
    public boolean updateGameState(long gameId, String stateJson) {
        String sql = "UPDATE games SET state_json = ?, last_played = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, stateJson);
            stmt.setLong(2, gameId);

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                LOGGER.info("Updated game ID: " + gameId);
                return true;
            }
        } catch (SQLException e) {
            LOGGER.severe("Error updating game: " + e.getMessage());
        }

        return false;
    }

    /**
     * Loads a game's state by ID.
     * 
     * @param gameId game ID
     * @return JSON state string, or null if not found
     */
    public String loadGame(long gameId) {
        String sql = "SELECT state_json FROM games WHERE id = ?";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, gameId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("state_json");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error loading game: " + e.getMessage());
        }

        return null;
    }

    /**
     * Marks a game as finished.
     * 
     * @param gameId game ID
     * @return true if updated, false otherwise
     */
    public boolean markGameFinished(long gameId) {
        String sql = "UPDATE games SET is_finished = TRUE WHERE id = ?";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, gameId);
            int affected = stmt.executeUpdate();

            return affected > 0;

        } catch (SQLException e) {
            LOGGER.severe("Error marking game finished: " + e.getMessage());
        }

        return false;
    }

    /**
     * Gets all game IDs of a specific type.
     * 
     * @param gameType        game type filter (e.g., "GOOSE", "CHECKERS")
     * @param includeFinished whether to include finished games
     * @return list of game IDs
     */
    public List<Long> getGamesByType(String gameType, boolean includeFinished) {
        List<Long> gameIds = new ArrayList<>();
        String sql = includeFinished
                ? "SELECT id FROM games WHERE game_type = ? ORDER BY last_played DESC"
                : "SELECT id FROM games WHERE game_type = ? AND is_finished = FALSE ORDER BY last_played DESC";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, gameType);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    gameIds.add(rs.getLong("id"));
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error getting games by type: " + e.getMessage());
        }

        return gameIds;
    }

    /**
     * Deletes a game by ID.
     * 
     * @param gameId game ID
     * @return true if deleted, false otherwise
     */
    public boolean deleteGame(long gameId) {
        String sql = "DELETE FROM games WHERE id = ?";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, gameId);
            int affected = stmt.executeUpdate();

            if (affected > 0) {
                LOGGER.info("Deleted game ID: " + gameId);
                return true;
            }
        } catch (SQLException e) {
            LOGGER.severe("Error deleting game: " + e.getMessage());
        }

        return false;
    }

    /**
     * Associates a user with a game as a player.
     * 
     * @param gameId       game ID
     * @param userId       user ID
     * @param playerNumber player number (1, 2, 3, 4)
     * @return true if associated, false otherwise
     */
    public boolean addPlayerToGame(long gameId, long userId, int playerNumber) {
        String sql = "INSERT INTO game_players (game_id, user_id, player_number) VALUES (?, ?, ?)";

        try (Connection conn = dbManager.getConnectionInternal();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, gameId);
            stmt.setLong(2, userId);
            stmt.setInt(3, playerNumber);

            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            LOGGER.severe("Error adding player to game: " + e.getMessage());
        }

        return false;
    }
}
