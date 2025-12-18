/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.logic.games.goose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Enhanced unit tests for GooseRules.
 */
class GooseRulesTest {

    private GooseRules rules;

    @BeforeEach
    void setUp() {
        rules = new GooseRules();
    }

    @Test
    @DisplayName("Should initialize goose rules")
    void shouldInitialize() {
        assertNotNull(rules);
    }

    @Test
    @DisplayName("Should not be finished at start")
    void shouldNotBeFinishedAtStart() {
        assertFalse(rules.isFinished());
    }

    @Test
    @DisplayName("Should support 2-6 players")
    void shouldSupportMultiplePlayers() {
        assertEquals(2, rules.getMinPlayers());
        assertEquals(6, rules.getMaxPlayers());
    }

    @Test
    @DisplayName("Should have game name")
    void shouldHaveGameName() {
        assertNotNull(rules.getGameName());
        assertTrue(rules.getGameName().toLowerCase().contains("goose"));
    }

    @Test
    @DisplayName("Board should have 63 squares")
    void shouldHave63Squares() {
        assertEquals(63, rules.getBoardSize());
    }
}
