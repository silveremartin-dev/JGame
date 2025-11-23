/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
 *
 * Enhanced with AI assistance from Google Gemini (Antigravity)
 */

package org.jgame.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jgame.logic.games.checkers.CheckersRules;
import org.jgame.logic.games.goose.GooseRules;
import org.jgame.persistence.dao.GameDAO;

import java.util.logging.Logger;

/**
 * Utility class for saving and loading game states using JSON serialization.
 * 
 * <p>
 * Uses Gson to serialize game objects to JSON strings for database storage.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class GameStateManager {

    private static final Logger LOGGER = Logger.getLogger(GameStateManager.class.getName());
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final GameDAO gameDAO = new GameDAO();

    // Prevent instantiation
    private GameStateManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Saves a Goose game to the database.
     * 
     * @param game the game to save
     * @return game ID if saved successfully, -1 otherwise
     */
    public static long saveGooseGame(GooseRules game) {
        try {
            String json = gson.toJson(game);
            long gameId = gameDAO.saveGame("GOOSE", json);

            if (gameId > 0) {
                LOGGER.info("Saved Goose game with ID: " + gameId);
            }
            return gameId;

        } catch (Exception e) {
            LOGGER.severe("Error saving Goose game: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Loads a Goose game from the database.
     * 
     * @param gameId the game ID to load
     * @return loaded game, or null if not found
     */
    public static GooseRules loadGooseGame(long gameId) {
        try {
            String json = gameDAO.loadGame(gameId);
            if (json == null) {
                LOGGER.warning("Game not found with ID: " + gameId);
                return null;
            }

            GooseRules game = gson.fromJson(json, GooseRules.class);
            LOGGER.info("Loaded Goose game with ID: " + gameId);
            return game;

        } catch (Exception e) {
            LOGGER.severe("Error loading Goose game: " + e.getMessage());
            return null;
        }
    }

    /**
     * Saves a Checkers game to the database.
     * 
     * @param game the game to save
     * @return game ID if saved successfully, -1 otherwise
     */
    public static long saveCheckersGame(CheckersRules game) {
        try {
            String json = gson.toJson(game);
            long gameId = gameDAO.saveGame("CHECKERS", json);

            if (gameId > 0) {
                LOGGER.info("Saved Checkers game with ID: " + gameId);
            }
            return gameId;

        } catch (Exception e) {
            LOGGER.severe("Error saving Checkers game: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Loads a Checkers game from the database.
     * 
     * @param gameId the game ID to load
     * @return loaded game, or null if not found
     */
    public static CheckersRules loadCheckersGame(long gameId) {
        try {
            String json = gameDAO.loadGame(gameId);
            if (json == null) {
                LOGGER.warning("Game not found with ID: " + gameId);
                return null;
            }

            CheckersRules game = gson.fromJson(json, CheckersRules.class);
            LOGGER.info("Loaded Checkers game with ID: " + gameId);
            return game;

        } catch (Exception e) {
            LOGGER.severe("Error loading Checkers game: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing game's state in the database.
     * 
     * @param gameId the game ID to update
     * @param game   the game object with new state
     * @return true if updated successfully, false otherwise
     */
    public static boolean updateGameState(long gameId, Object game) {
        try {
            String json = gson.toJson(game);
            boolean success = gameDAO.updateGameState(gameId, json);

            if (success) {
                LOGGER.info("Updated game state for ID: " + gameId);
            }
            return success;

        } catch (Exception e) {
            LOGGER.severe("Error updating game state: " + e.getMessage());
            return false;
        }
    }

    /**
     * Auto-saves a game (called on significant game events).
     * 
     * @param gameId the game ID
     * @param game   the game object
     */
    public static void autoSave(long gameId, Object game) {
        if (gameId > 0) {
            updateGameState(gameId, game);
        }
    }
}
