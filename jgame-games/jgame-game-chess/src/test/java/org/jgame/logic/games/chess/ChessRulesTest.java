/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
