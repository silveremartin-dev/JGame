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
 * Unit tests for RandomAI class.
 */
class RandomAITest {

    @Test
    @DisplayName("RandomAI should be instantiable")
    void shouldCreateRandomAI() {
        RandomAI ai = new RandomAI();
        assertNotNull(ai);
    }

    @Test
    @DisplayName("RandomAI should have a name")
    void shouldHaveName() {
        RandomAI ai = new RandomAI();
        assertNotNull(ai.getName());
        assertFalse(ai.getName().isEmpty());
    }
}
