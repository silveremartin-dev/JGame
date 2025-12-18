/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.server.lobby;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Manages game lobbies.
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class LobbyManager {

    private static final Logger logger = LogManager.getLogger(LobbyManager.class);
    private static LobbyManager instance;

    private final Map<String, GameLobby> lobbies = new ConcurrentHashMap<>();
    private final Map<String, String> userToLobby = new ConcurrentHashMap<>();

    private LobbyManager() {
    }

    public static synchronized LobbyManager getInstance() {
        if (instance == null) {
            instance = new LobbyManager();
        }
        return instance;
    }

    /**
     * Creates a new lobby.
     */
    public GameLobby createLobby(String gameId, String hostId, String name, int maxPlayers) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        GameLobby lobby = new GameLobby(id, gameId, hostId, name, maxPlayers);
        lobbies.put(id, lobby);
        userToLobby.put(hostId, id);
        logger.info("Created lobby {} for game {}", id, gameId);
        return lobby;
    }

    /**
     * Gets a lobby by ID.
     */
    public Optional<GameLobby> getLobby(String id) {
        return Optional.ofNullable(lobbies.get(id));
    }

    /**
     * Gets the lobby a user is in.
     */
    public Optional<GameLobby> getUserLobby(String userId) {
        String lobbyId = userToLobby.get(userId);
        return lobbyId != null ? getLobby(lobbyId) : Optional.empty();
    }

    /**
     * Joins a lobby.
     */
    public boolean joinLobby(String lobbyId, String userId, String password) {
        GameLobby lobby = lobbies.get(lobbyId);
        if (lobby == null)
            return false;
        if (!lobby.checkPassword(password))
            return false;
        if (!lobby.addPlayer(userId))
            return false;
        userToLobby.put(userId, lobbyId);
        logger.info("User {} joined lobby {}", userId, lobbyId);
        return true;
    }

    /**
     * Leaves a lobby.
     */
    public void leaveLobby(String userId) {
        String lobbyId = userToLobby.remove(userId);
        if (lobbyId != null) {
            GameLobby lobby = lobbies.get(lobbyId);
            if (lobby != null) {
                lobby.removePlayer(userId);
                // Remove lobby if empty or host left
                if (lobby.getPlayerCount() == 0 || userId.equals(lobby.getHostId())) {
                    lobbies.remove(lobbyId);
                    logger.info("Lobby {} removed", lobbyId);
                }
            }
        }
    }

    /**
     * Gets all waiting lobbies for a game.
     */
    public List<GameLobby> getLobbiesForGame(String gameId) {
        return lobbies.values().stream()
                .filter(l -> l.getGameId().equals(gameId))
                .filter(l -> l.getState() == GameLobby.LobbyState.WAITING)
                .collect(Collectors.toList());
    }

    /**
     * Gets all waiting lobbies.
     */
    public List<GameLobby> getAllWaitingLobbies() {
        return lobbies.values().stream()
                .filter(l -> l.getState() == GameLobby.LobbyState.WAITING)
                .collect(Collectors.toList());
    }

    /**
     * Starts a lobby game.
     */
    public boolean startLobby(String lobbyId, String userId) {
        GameLobby lobby = lobbies.get(lobbyId);
        if (lobby == null)
            return false;
        if (!lobby.getHostId().equals(userId))
            return false;
        if (!lobby.canStart())
            return false;
        lobby.start();
        logger.info("Lobby {} started", lobbyId);
        return true;
    }
}
