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
package org.jgame.plugin;

import java.util.Map;

/**
 * Descriptor for a game plugin containing metadata and configuration.
 * 
 * <p>
 * Using Java 21 record for immutable game descriptor.
 * </p>
 * 
 * @param id           unique game identifier (e.g., "goose", "checkers")
 * @param name         display name (e.g., "Game of the Goose")
 * @param version      plugin version
 * @param author       plugin author
 * @param description  short description
 * @param instructions detailed playing instructions
 * @param minPlayers   minimum number of players
 * @param maxPlayers   maximum number of players
 * @param metadata     additional metadata (icon path, difficulty, etc.)
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public record GameDescriptor(
        String id,
        String name,
        String version,
        String author,
        String description,
        String instructions,
        int minPlayers,
        int maxPlayers,
        Map<String, Object> metadata) {
    /**
     * Compact constructor with validation.
     */
    public GameDescriptor {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Game ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Game name cannot be null or blank");
        }
        if (minPlayers < 1) {
            throw new IllegalArgumentException("Minimum players must be at least 1");
        }
        if (maxPlayers < minPlayers) {
            throw new IllegalArgumentException("Max players must be >= min players");
        }
        // Make metadata immutable
        metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
    }

    /**
     * Checks if the given player count is valid for this game.
     * 
     * @param playerCount number of players
     * @return true if player count is valid
     */
    public boolean isValidPlayerCount(int playerCount) {
        return playerCount >= minPlayers && playerCount <= maxPlayers;
    }

    /**
     * Gets metadata value by key.
     * 
     * @param key metadata key
     * @return value or null if not found
     */
    public Object getMetadata(String key) {
        return metadata.get(key);
    }
}
