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
