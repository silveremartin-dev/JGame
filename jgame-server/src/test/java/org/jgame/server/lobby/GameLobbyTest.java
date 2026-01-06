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
