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

package org.jgame.server.persistence.dto;

import java.time.Instant;

/**
 * Data Transfer Object for player scores.
 * 
 * <p>
 * Using Java 21 record for immutable, concise data structure.
 * </p>
 * 
 * @param userId    the user's ID
 * @param username  the user's username
 * @param score     the score achieved
 * @param position  the final position/rank (1 = winner)
 * @param timestamp when the score was recorded
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public record PlayerScore(
        long userId,
        String username,
        double score,
        int position,
        Instant timestamp) {
    /**
     * Compact constructor with validation.
     */
    public PlayerScore {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (position < 1) {
            throw new IllegalArgumentException("Position must be at least 1");
        }
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    /**
     * Checks if this score represents a win (1st place).
     * 
     * @return true if position is 1
     */
    public boolean isWin() {
        return position == 1;
    }
}
