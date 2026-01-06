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
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameRating record.
 */
class GameRatingTest {

    @Test
    @DisplayName("Should create valid GameRating")
    void shouldCreateValidGameRating() {
        Instant now = Instant.now();
        GameRating rating = new GameRating(
                "user1", "chess", 5, "Great game!", now, now);

        assertEquals("user1", rating.userId());
        assertEquals("chess", rating.gameId());
        assertEquals(5, rating.stars());
        assertEquals("Great game!", rating.comment());
    }

    @Test
    @DisplayName("Should create rating using factory")
    void shouldCreateUsingFactory() {
        GameRating rating = GameRating.create("user1", "chess", 4, "Fun!");

        assertEquals("user1", rating.userId());
        assertEquals(4, rating.stars());
        assertTrue(rating.hasComment());
    }

    @Test
    @DisplayName("Should reject invalid star ratings")
    void shouldRejectInvalidStars() {
        // Stars must be 1-5
        assertThrows(IllegalArgumentException.class, () -> GameRating.create("user1", "chess", 0, ""));

        assertThrows(IllegalArgumentException.class, () -> GameRating.create("user1", "chess", 6, ""));
    }

    @Test
    @DisplayName("Should allow empty comment")
    void shouldAllowEmptyComment() {
        GameRating rating = GameRating.create("user1", "chess", 4, "");

        assertEquals("", rating.comment());
        assertFalse(rating.hasComment());
    }

    @Test
    @DisplayName("Should update stars")
    void shouldUpdateStars() {
        GameRating rating = GameRating.create("user1", "chess", 3, "OK");
        GameRating updated = rating.withStars(5);

        assertEquals(5, updated.stars());
        assertEquals("OK", updated.comment());
    }

    @Test
    @DisplayName("Should update comment")
    void shouldUpdateComment() {
        GameRating rating = GameRating.create("user1", "chess", 4, "Good");
        GameRating updated = rating.withComment("Great!");

        assertEquals(4, updated.stars());
        assertEquals("Great!", updated.comment());
    }
}
