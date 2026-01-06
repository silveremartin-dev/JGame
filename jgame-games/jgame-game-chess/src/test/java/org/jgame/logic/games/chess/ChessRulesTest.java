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
package org.jgame.logic.games.chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ChessRules.
 */
class ChessRulesTest {

    private ChessRules rules;

    @BeforeEach
    void setUp() {
        rules = new ChessRules();
    }

    @Test
    @DisplayName("Should initialize with empty board state")
    void shouldInitialize() {
        assertNotNull(rules);
    }

    @Test
    @DisplayName("Should not be finished at start")
    void shouldNotBeFinishedAtStart() {
        assertFalse(rules.isFinished());
    }

    @Test
    @DisplayName("Should have no winner at start")
    void shouldHaveNoWinnerAtStart() {
        assertNull(rules.getWinner());
    }

    @Test
    @DisplayName("Should support exactly 2 players")
    void shouldSupportTwoPlayers() {
        // Chess requires exactly 2 players
        assertEquals(2, rules.getMinPlayers(), "Chess should require minimum 2 players");
        assertEquals(2, rules.getMaxPlayers(), "Chess should allow maximum 2 players");
    }

    @Test
    @DisplayName("Should have game name")
    void shouldHaveGameName() {
        assertNotNull(rules.getGameName());
        assertFalse(rules.getGameName().isEmpty());
    }
}
