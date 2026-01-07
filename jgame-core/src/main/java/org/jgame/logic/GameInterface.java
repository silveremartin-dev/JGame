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
package org.jgame.logic;

import org.jgame.parts.PieceInterface;
import org.jgame.parts.PlayerInterface;

import java.util.Set;

public interface GameInterface {

    String getName();

    String getVersion();

    String getDescription();

    Set<PieceInterface> getPieces();

    Set<Rule> getRuleset();

    /**
     * Initializes the game state.
     */
    default void initialize() {
        // Default: no initialization needed
    }

    /**
     * Checks if the game is finished.
     *
     * @return true if game is over
     */
    default boolean isFinished() {
        return false;
    }

    /**
     * Gets legal actions for a player.
     *
     * @param player the player
     * @return set of legal actions
     */
    default Set<ActionInterface> getLegalActions(PlayerInterface player) {
        return java.util.Collections.emptySet();
    }

    /**
     * Executes an action for a player.
     *
     * @param player the player
     * @param action the action
     */
    default void executeAction(PlayerInterface player, ActionInterface action) {
        // Default: no action
    }

    /**
     * Undoes an action.
     *
     * @param action the action to undo
     */
    default void undoAction(ActionInterface action) {
        // Default: no undo
    }

    /**
     * Gets a player's score.
     *
     * @param player the player
     * @return the score
     */
    default ScoreInterface getScore(PlayerInterface player) {
        return null;
    }

    /**
     * Converts current game state to a serializable record.
     * 
     * @return GameState record
     */
    default org.jgame.logic.engine.GameState toGameState() {
        return null; // Subclasses should implement
    }
}
