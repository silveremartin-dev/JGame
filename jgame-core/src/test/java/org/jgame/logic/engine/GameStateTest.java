/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.logic.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for GameState record.
 */
class GameStateTest {

    @Test
    @DisplayName("GameState should be instantiable with required parameters")
    void shouldCreateGameState() {
        GameState state = new GameState(
                "chess",
                "session-001",
                List.of("player1", "player2"),
                0,
                1,
                GameState.GamePhase.PLAYING,
                Map.of(),
                Map.of(),
                List.of(),
                Instant.now(),
                Instant.now());
        assertNotNull(state);
        assertEquals("chess", state.gameId());
    }

    @Test
    @DisplayName("GameState should track if game is ended")
    void shouldTrackGameEnded() {
        GameState state = new GameState(
                "chess",
                "session-001",
                List.of("player1", "player2"),
                0,
                1,
                GameState.GamePhase.PLAYING,
                Map.of(),
                Map.of(),
                List.of(),
                Instant.now(),
                Instant.now());
        assertFalse(state.isEnded());
    }

    @Test
    @DisplayName("GameState should track current player")
    void shouldTrackCurrentPlayer() {
        GameState state = new GameState(
                "chess",
                "session-001",
                List.of("player1", "player2"),
                0,
                1,
                GameState.GamePhase.PLAYING,
                Map.of(),
                Map.of(),
                List.of(),
                Instant.now(),
                Instant.now());
        assertEquals("player1", state.currentPlayerId());
        assertTrue(state.isPlayerTurn("player1"));
    }

    @Test
    @DisplayName("GameState FINISHED phase should report as ended")
    void shouldReportFinishedAsEnded() {
        GameState state = new GameState(
                "chess",
                "session-001",
                List.of("player1", "player2"),
                0,
                10,
                GameState.GamePhase.FINISHED,
                Map.of(),
                Map.of(),
                List.of(),
                Instant.now(),
                Instant.now());
        assertTrue(state.isEnded());
        assertFalse(state.isActive());
    }
}
