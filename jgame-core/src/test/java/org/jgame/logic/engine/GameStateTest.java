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
