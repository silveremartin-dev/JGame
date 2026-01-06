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
import java.util.Map;

/**
 * Unit tests for GameAction record.
 */
class GameActionTest {

    @Test
    @DisplayName("GameAction should be instantiable")
    void shouldCreateGameAction() {
        GameAction action = new GameAction("action-001", "player1", "MOVE", Map.of(), Instant.now());
        assertNotNull(action);
    }

    @Test
    @DisplayName("GameAction should store action type")
    void shouldStoreActionType() {
        GameAction action = new GameAction("action-001", "player1", "MOVE", Map.of(), Instant.now());
        assertEquals("MOVE", action.actionType());
    }

    @Test
    @DisplayName("GameAction should store player id")
    void shouldStorePlayerId() {
        GameAction action = new GameAction("action-001", "player1", "MOVE", Map.of(), Instant.now());
        assertEquals("player1", action.playerId());
    }

    @Test
    @DisplayName("GameAction should have timestamp")
    void shouldHaveTimestamp() {
        GameAction action = new GameAction("action-001", "player1", "MOVE", Map.of(), Instant.now());
        assertNotNull(action.timestamp());
    }

    @Test
    @DisplayName("GameAction.move should create move action")
    void shouldCreateMoveAction() {
        GameAction action = GameAction.move("player1", "e2", "e4");
        assertEquals("MOVE", action.actionType());
        assertEquals("e2", action.getParameter("from"));
        assertEquals("e4", action.getParameter("to"));
    }

    @Test
    @DisplayName("GameAction.pass should create pass action")
    void shouldCreatePassAction() {
        GameAction action = GameAction.pass("player1");
        assertEquals("PASS", action.actionType());
    }
}
