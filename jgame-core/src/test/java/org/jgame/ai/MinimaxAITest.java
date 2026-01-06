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
