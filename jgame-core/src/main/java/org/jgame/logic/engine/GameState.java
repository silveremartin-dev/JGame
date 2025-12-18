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
 */

package org.jgame.logic.engine;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Represents a serializable snapshot of game state.
 *
 * <p>
 * Used for saving/loading games and transmitting state between client/server.
 * </p>
 *
 * @param gameId             the game type identifier
 * @param sessionId          unique session identifier
 * @param playerIds          list of player identifiers in turn order
 * @param currentPlayerIndex index of the player whose turn it is
 * @param turnNumber         current turn number
 * @param phase              current game phase (SETUP, PLAYING, FINISHED)
 * @param boardState         serialized board state (game-specific)
 * @param playerStates       per-player state data
 * @param history            list of actions taken
 * @param createdAt          when the game started
 * @param updatedAt          when state was last modified
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public record GameState(
        String gameId,
        String sessionId,
        List<String> playerIds,
        int currentPlayerIndex,
        int turnNumber,
        GamePhase phase,
        Map<String, Object> boardState,
        Map<String, Object> playerStates,
        List<GameAction> history,
        Instant createdAt,
        Instant updatedAt) implements Serializable {

    /**
     * Game phases.
     */
    public enum GamePhase {
        /** Game setup in progress */
        SETUP,
        /** Game is actively being played */
        PLAYING,
        /** Game has ended */
        FINISHED,
        /** Game was abandoned */
        ABANDONED
    }

    /**
     * Compact constructor with validation.
     */
    public GameState {
        if (gameId == null || gameId.isBlank()) {
            throw new IllegalArgumentException("Game ID cannot be null or blank");
        }
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("Session ID cannot be null or blank");
        }
        if (playerIds == null || playerIds.isEmpty()) {
            throw new IllegalArgumentException("Player IDs cannot be null or empty");
        }
        if (currentPlayerIndex < 0 || currentPlayerIndex >= playerIds.size()) {
            throw new IllegalArgumentException("Current player index out of bounds");
        }
        if (phase == null) {
            phase = GamePhase.SETUP;
        }
        // Make collections immutable
        playerIds = List.copyOf(playerIds);
        boardState = boardState != null ? Map.copyOf(boardState) : Map.of();
        playerStates = playerStates != null ? Map.copyOf(playerStates) : Map.of();
        history = history != null ? List.copyOf(history) : List.of();
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = createdAt;
        }
    }

    /**
     * Gets the current player's ID.
     *
     * @return current player ID
     */
    public String currentPlayerId() {
        return playerIds.get(currentPlayerIndex);
    }

    /**
     * Checks if it's the specified player's turn.
     *
     * @param playerId player to check
     * @return true if it's this player's turn
     */
    public boolean isPlayerTurn(String playerId) {
        return currentPlayerId().equals(playerId);
    }

    /**
     * Checks if the game is still in progress.
     *
     * @return true if game is active
     */
    public boolean isActive() {
        return phase == GamePhase.PLAYING;
    }

    /**
     * Checks if the game has ended.
     *
     * @return true if game is finished or abandoned
     */
    public boolean isEnded() {
        return phase == GamePhase.FINISHED || phase == GamePhase.ABANDONED;
    }
}
