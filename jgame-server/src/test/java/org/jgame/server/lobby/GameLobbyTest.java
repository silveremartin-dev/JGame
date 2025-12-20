/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.server.lobby;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameLobby.
 */
class GameLobbyTest {

    @Test
    @DisplayName("Should create lobby with correct initial state")
    void shouldCreateLobbyWithCorrectState() {
        GameLobby lobby = new GameLobby("lobby1", "chess", "host1", "Test Lobby", 4);

        assertEquals("lobby1", lobby.getId());
        assertEquals("chess", lobby.getGameId());
        assertEquals("host1", lobby.getHostId());
        assertEquals("Test Lobby", lobby.getName());
        assertEquals(4, lobby.getMaxPlayers());
        assertEquals(1, lobby.getPlayerCount()); // Host is first player
        assertEquals(GameLobby.LobbyState.WAITING, lobby.getState());
    }

    @Test
    @DisplayName("Should add player successfully")
    void shouldAddPlayer() {
        GameLobby lobby = new GameLobby("lobby1", "chess", "host1", "Test", 4);

        boolean added = lobby.addPlayer("player2");

        assertTrue(added);
        assertEquals(2, lobby.getPlayerCount());
    }

    @Test
    @DisplayName("Should not add player when full")
    void shouldNotAddPlayerWhenFull() {
        GameLobby lobby = new GameLobby("lobby1", "chess", "host1", "Test", 2);
        lobby.addPlayer("player2");

        boolean added = lobby.addPlayer("player3");

        assertFalse(added);
        assertEquals(2, lobby.getPlayerCount());
    }

    @Test
    @DisplayName("Should remove player successfully")
    void shouldRemovePlayer() {
        GameLobby lobby = new GameLobby("lobby1", "chess", "host1", "Test", 4);
        lobby.addPlayer("player2");

        lobby.removePlayer("player2");

        assertEquals(1, lobby.getPlayerCount());
    }

    @Test
    @DisplayName("Should check password correctly")
    void shouldCheckPassword() {
        GameLobby lobby = new GameLobby("lobby1", "chess", "host1", "Test", 4);
        lobby.setPassword("secret");

        assertTrue(lobby.checkPassword("secret"));
        assertFalse(lobby.checkPassword("wrong"));
        assertFalse(lobby.checkPassword(null));
    }

    @Test
    @DisplayName("Should allow null password when no password set")
    void shouldAllowNullPasswordWhenNotSet() {
        GameLobby lobby = new GameLobby("lobby1", "chess", "host1", "Test", 4);

        assertTrue(lobby.checkPassword(null));
        assertTrue(lobby.checkPassword(""));
    }

    @Test
    @DisplayName("Should check if can start")
    void shouldCheckCanStart() {
        GameLobby lobby = new GameLobby("lobby1", "chess", "host1", "Test", 2);

        assertFalse(lobby.canStart()); // Only 1 player

        lobby.addPlayer("player2");
        assertTrue(lobby.canStart()); // Now 2 players
    }

    @Test
    @DisplayName("Should start lobby")
    void shouldStartLobby() {
        GameLobby lobby = new GameLobby("lobby1", "chess", "host1", "Test", 2);
        lobby.addPlayer("player2");

        lobby.start();

        assertEquals(GameLobby.LobbyState.STARTING, lobby.getState());
    }
}
