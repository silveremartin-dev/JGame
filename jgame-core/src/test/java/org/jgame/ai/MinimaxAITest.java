/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MinimaxAI class.
 */
class MinimaxAITest {

    @Test
    @DisplayName("MinimaxAI should be instantiable")
    void shouldCreateMinimaxAI() {
        MinimaxAI ai = new MinimaxAI(3);
        assertNotNull(ai);
    }

    @Test
    @DisplayName("MinimaxAI should have a name")
    void shouldHaveName() {
        MinimaxAI ai = new MinimaxAI("TestAI", 3);
        assertNotNull(ai.getName());
        assertEquals("TestAI", ai.getName());
    }

    @Test
    @DisplayName("MinimaxAI should support different difficulty levels")
    void shouldSupportDifficultyLevels() {
        MinimaxAI easy = MinimaxAI.easy();
        MinimaxAI medium = MinimaxAI.medium();
        MinimaxAI hard = MinimaxAI.hard();

        assertTrue(easy.getDifficulty() < medium.getDifficulty());
        assertTrue(medium.getDifficulty() < hard.getDifficulty());
    }

    @Test
    @DisplayName("MinimaxAI factory methods should create instances")
    void shouldCreateViaFactory() {
        assertNotNull(MinimaxAI.easy());
        assertNotNull(MinimaxAI.medium());
        assertNotNull(MinimaxAI.hard());
    }
}
