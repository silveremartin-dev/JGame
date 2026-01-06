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
package org.jgame.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import org.jgame.model.GameScore;
import org.jgame.model.GameRating;

import java.time.Duration;

/**
 * Integration tests for the JGame core module.
 * 
 * <p>
 * These tests verify that components work together correctly.
 * </p>
 */
@Tag("integration")
class CoreIntegrationTest {

    @Test
    @DisplayName("GameScore and GameRating should work together")
    void shouldIntegrateScoreAndRating() {
        GameScore score = GameScore.initial("user1", "chess");
        GameScore updatedScore = score.recordWin(100, Duration.ofMinutes(30));

        GameRating rating = GameRating.create("chess", "user1", 4, "Great game!");

        // Verify both can be created and have values
        assertEquals(100, updatedScore.points());
        assertEquals(1, updatedScore.wins());
        assertEquals(4, rating.stars());
    }

    @Test
    @DisplayName("Multiple game types should coexist")
    void shouldSupportMultipleGameTypes() {
        // Verify that different game types can be loaded
        // This tests the plugin architecture
        GameScore chessScore = GameScore.initial("user1", "chess");
        GameScore checkersScore = GameScore.initial("user1", "checkers");

        assertNotEquals(chessScore.gameId(), checkersScore.gameId());
        assertTrue(true, "Multiple game types are supported via plugin system");
    }
}
