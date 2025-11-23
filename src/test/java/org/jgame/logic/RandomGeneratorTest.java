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

package org.jgame.logic;

import org.jgame.util.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the RandomGenerator utility class.
 *
 * @author Silvere Martin-Michiellot
 */
@DisplayName("RandomGenerator Tests")
class RandomGeneratorTest {

    @Test
    @DisplayName("Should roll dice with valid parameters")
    void testRollDices() {
        List<Integer> result = RandomGenerator.rollDices(6, 2);
        assertNotNull(result);
        assertEquals(2, result.size());

        for (Integer roll : result) {
            assertTrue(roll >= 1 && roll <= 6, "Dice roll should be between 1 and 6");
        }
    }

    @RepeatedTest(10)
    @DisplayName("Should generate random values within range (repeated)")
    void testRandomValuesInRange() {
        List<Integer> result = RandomGenerator.rollDices(6, 2);

        for (Integer roll : result) {
            assertTrue(roll >= 1 && roll <= 6);
        }
    }

    @Test
    @DisplayName("Should roll single die")
    void testRollSingleDie() {
        List<Integer> result = RandomGenerator.rollDices(6, 1);
        assertEquals(1, result.size());
        assertTrue(result.get(0) >= 1 && result.get(0) <= 6);
    }

    @Test
    @DisplayName("Should roll multiple dice")
    void testRollMultipleDice() {
        int numberOfDice = 5;
        List<Integer> result = RandomGenerator.rollDices(6, numberOfDice);
        assertEquals(numberOfDice, result.size());
    }

    @Test
    @DisplayName("Should handle different die sizes")
    void testDifferentDieSizes() {
        // D4
        List<Integer> d4 = RandomGenerator.rollDices(4, 1);
        assertTrue(d4.get(0) >= 1 && d4.get(0) <= 4);

        // D8
        List<Integer> d8 = RandomGenerator.rollDices(8, 1);
        assertTrue(d8.get(0) >= 1 && d8.get(0) <= 8);

        // D12
        List<Integer> d12 = RandomGenerator.rollDices(12, 1);
        assertTrue(d12.get(0) >= 1 && d12.get(0) <= 12);

        // D20
        List<Integer> d20 = RandomGenerator.rollDices(20, 1);
        assertTrue(d20.get(0) >= 1 && d20.get(0) <= 20);
    }
}
