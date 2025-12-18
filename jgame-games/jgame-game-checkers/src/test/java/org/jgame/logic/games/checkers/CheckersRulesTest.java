/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.logic.games.checkers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Enhanced unit tests for CheckersRules.
 */
class CheckersRulesTest {

    private CheckersRules rules;

    @BeforeEach
    void setUp() {
        rules = new CheckersRules();
    }

    @Test
    @DisplayName("Should initialize checkers rules")
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
        assertEquals(2, rules.getMinPlayers());
        assertEquals(2, rules.getMaxPlayers());
    }

    @Test
    @DisplayName("Should have game name")
    void shouldHaveGameName() {
        assertNotNull(rules.getGameName());
        assertEquals("Checkers", rules.getGameName());
    }
}
