/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.ai;

import org.jgame.logic.engine.GameAction;
import org.jgame.logic.engine.GameState;

/**
 * Interface for AI players in games.
 *
 * <p>
 * Implementations can range from simple random selection
 * to complex algorithms like minimax.
 * </p>
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface GameAI {

    /**
     * Gets the AI name.
     *
     * @return AI name
     */
    String getName();

    /**
     * Gets the AI difficulty level.
     *
     * @return difficulty (1-10)
     */
    int getDifficulty();

    /**
     * Computes the best move for the current game state.
     *
     * @param state current game state
     * @return the chosen action, or null if no valid moves
     */
    GameAction computeMove(GameState state);

    /**
     * Called when a new game starts.
     *
     * @param gameId the game identifier
     */
    default void onGameStart(String gameId) {
        // Optional hook
    }

    /**
     * Called when the game ends.
     *
     * @param won whether the AI won
     */
    default void onGameEnd(boolean won) {
        // Optional hook for learning AIs
    }
}
