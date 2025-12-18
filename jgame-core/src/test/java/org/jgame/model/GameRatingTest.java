/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
