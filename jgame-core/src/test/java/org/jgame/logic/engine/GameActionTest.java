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
