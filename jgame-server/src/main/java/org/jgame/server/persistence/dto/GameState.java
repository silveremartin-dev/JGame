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
package org.jgame.server.persistence.dto;

import java.time.Instant;
import java.util.List;

/**
 * Data Transfer Object for game state metadata.
 * 
 * <p>
 * Using Java 21 record for immutable game state information.
 * </p>
 * 
 * @param gameId          the game's unique ID
 * @param gameType        the type of game (e.g., "GOOSE", "CHECKERS")
 * @param playerUsernames list of player usernames
 * @param isFinished      whether the game has ended
 * @param createdAt       when the game was created
 * @param lastPlayedAt    when the game was last played
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public record GameState(
        long gameId,
        String gameType,
        List<String> playerUsernames,
        boolean isFinished,
        Instant createdAt,
        Instant lastPlayedAt) {
    /**
     * Compact constructor with validation.
     */
    public GameState {
        if (gameType == null || gameType.isBlank()) {
            throw new IllegalArgumentException("Game type cannot be null or blank");
        }
        if (playerUsernames == null || playerUsernames.isEmpty()) {
            throw new IllegalArgumentException("Must have at least one player");
        }
        // Make list immutable
        playerUsernames = List.copyOf(playerUsernames);
    }

    /**
     * Gets the number of players in this game.
     * 
     * @return player count
     */
    public int getPlayerCount() {
        return playerUsernames.size();
    }
}
