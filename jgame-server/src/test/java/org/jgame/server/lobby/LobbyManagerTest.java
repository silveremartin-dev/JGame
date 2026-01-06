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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LobbyManager.
 */
class LobbyManagerTest {

    private LobbyManager manager;

    @BeforeEach
    void setUp() {
        manager = LobbyManager.getInstance();
        manager.clearAll();
    }

    @Test
    @DisplayName("Should create lobby successfully")
    void shouldCreateLobby() {
        GameLobby lobby = manager.createLobby("chess", "host1", "Test Lobby", 4);

        assertNotNull(lobby);
        assertEquals("chess", lobby.getGameId());
        assertEquals("host1", lobby.getHostId());
        assertEquals("Test Lobby", lobby.getName());
        assertEquals(4, lobby.getMaxPlayers());
    }

    @Test
    @DisplayName("Should get lobby by ID")
    void shouldGetLobbyById() {
        GameLobby created = manager.createLobby("chess", "host1", "Test", 2);

        Optional<GameLobby> found = manager.getLobby(created.getId());

        assertTrue(found.isPresent());
        assertEquals(created.getId(), found.get().getId());
    }

    @Test
    @DisplayName("Should return empty for non-existent lobby")
    void shouldReturnEmptyForNonExistentLobby() {
        Optional<GameLobby> found = manager.getLobby("nonexistent");

        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should prevent user from creating multiple lobbies")
    void shouldPreventMultipleLobbies() {
        manager.createLobby("chess", "host1", "Lobby1", 2);
        GameLobby second = manager.createLobby("checkers", "host1", "Lobby2", 2);

        assertNull(second, "User should not be able to create second lobby");
    }

    @Test
    @DisplayName("Should join lobby successfully")
    void shouldJoinLobby() {
        GameLobby lobby = manager.createLobby("chess", "host1", "Test", 4);

        boolean joined = manager.joinLobby(lobby.getId(), "player2", null);

        assertTrue(joined);
        assertEquals(2, lobby.getPlayerCount());
    }

    @Test
    @DisplayName("Should fail to join non-existent lobby")
    void shouldFailToJoinNonExistentLobby() {
        boolean joined = manager.joinLobby("nonexistent", "player1", null);

        assertFalse(joined);
    }

    @Test
    @DisplayName("Should leave lobby successfully")
    void shouldLeaveLobby() {
        GameLobby lobby = manager.createLobby("chess", "host1", "Test", 4);
        manager.joinLobby(lobby.getId(), "player2", null);

        manager.leaveLobby("player2");

        assertEquals(1, lobby.getPlayerCount());
    }

    @Test
    @DisplayName("Should remove lobby when host leaves")
    void shouldRemoveLobbyWhenHostLeaves() {
        GameLobby lobby = manager.createLobby("chess", "host1", "Test", 4);
        String lobbyId = lobby.getId();

        manager.leaveLobby("host1");

        assertFalse(manager.getLobby(lobbyId).isPresent());
    }

    @Test
    @DisplayName("Should get user lobby")
    void shouldGetUserLobby() {
        manager.createLobby("chess", "host1", "Test", 4);

        Optional<GameLobby> userLobby = manager.getUserLobby("host1");

        assertTrue(userLobby.isPresent());
    }

    @Test
    @DisplayName("Should get all waiting lobbies")
    void shouldGetAllWaitingLobbies() {
        manager.createLobby("chess", "host1", "Lobby1", 2);
        manager.createLobby("checkers", "host2", "Lobby2", 2);

        List<GameLobby> waiting = manager.getAllWaitingLobbies();

        assertEquals(2, waiting.size());
    }

    @Test
    @DisplayName("Should get lobbies for specific game")
    void shouldGetLobbiesForGame() {
        manager.createLobby("chess", "host1", "Chess1", 2);
        manager.createLobby("chess", "host2", "Chess2", 2);
        manager.createLobby("checkers", "host3", "Checkers1", 2);

        List<GameLobby> chessLobbies = manager.getLobbiesForGame("chess");

        assertEquals(2, chessLobbies.size());
    }

    @Test
    @DisplayName("Should count lobbies")
    void shouldCountLobbies() {
        assertEquals(0, manager.getLobbyCount());

        manager.createLobby("chess", "host1", "Lobby1", 2);
        assertEquals(1, manager.getLobbyCount());

        manager.createLobby("checkers", "host2", "Lobby2", 2);
        assertEquals(2, manager.getLobbyCount());
    }
}
