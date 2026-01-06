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
package org.jgame.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.Duration;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameScore record.
 */
class GameScoreTest {

    @Test
    @DisplayName("Should create valid GameScore")
    void shouldCreateValidGameScore() {
        GameScore score = new GameScore(
                "user1", "chess", 1500, 10, 7, 3,
                Duration.ofHours(5), Instant.now());

        assertEquals("user1", score.userId());
        assertEquals("chess", score.gameId());
        assertEquals(1500, score.points());
        assertEquals(10, score.gamesPlayed());
        assertEquals(7, score.wins());
        assertEquals(3, score.losses());
    }

    @Test
    @DisplayName("Should calculate win rate correctly")
    void shouldCalculateWinRate() {
        GameScore score = new GameScore(
                "user1", "chess", 1500, 10, 7, 3,
                Duration.ofHours(1), Instant.now());

        // 7 wins out of 10 games = 70%
        assertEquals(70.0, score.winRate(), 0.001);
    }

    @Test
    @DisplayName("Should create initial score")
    void shouldCreateInitialScore() {
        GameScore score = GameScore.initial("user1", "chess");

        assertEquals("user1", score.userId());
        assertEquals("chess", score.gameId());
        assertEquals(0, score.points());
        assertEquals(0, score.wins());
        assertEquals(0, score.losses());
    }

    @Test
    @DisplayName("Should record win")
    void shouldRecordWin() {
        GameScore score = GameScore.initial("user1", "chess");
        GameScore updated = score.recordWin(100, Duration.ofMinutes(30));

        assertEquals(1, updated.wins());
        assertEquals(100, updated.points());
        assertEquals(1, updated.gamesPlayed());
    }

    @Test
    @DisplayName("Should record loss")
    void shouldRecordLoss() {
        GameScore score = GameScore.initial("user1", "chess");
        GameScore updated = score.recordLoss(10, Duration.ofMinutes(15));

        assertEquals(1, updated.losses());
        assertEquals(10, updated.points());
    }

    @Test
    @DisplayName("Should reject null userId")
    void shouldRejectNullUserId() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameScore(null, "chess", 0, 0, 0, 0, Duration.ZERO, Instant.now()));
    }
}
