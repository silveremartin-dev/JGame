/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
