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
 * Manages game lobbies with thread-safe operations.
 * 
 * <p>
 * Uses atomic compute() operations to prevent race conditions.
 * </p>
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 2.0
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
     * Creates a new lobby atomically.
     */
    public GameLobby createLobby(String gameId, String hostId, String name, int maxPlayers) {
        // First check if user is already in a lobby
        if (userToLobby.containsKey(hostId)) {
            logger.warn("User {} already in a lobby", hostId);
            return null;
        }

        String id = UUID.randomUUID().toString().substring(0, 8);
        GameLobby lobby = new GameLobby(id, gameId, hostId, name, maxPlayers);

        // Atomic put - only succeeds if key doesn't exist
        GameLobby existing = lobbies.putIfAbsent(id, lobby);
        if (existing != null) {
            // Extremely rare UUID collision
            logger.error("Lobby ID collision: {}", id);
            return null;
        }

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
     * Joins a lobby atomically to prevent race conditions.
     */
    public boolean joinLobby(String lobbyId, String userId, String password) {
        // Check if user is already in a lobby
        if (userToLobby.containsKey(userId)) {
            logger.warn("User {} already in a lobby", userId);
            return false;
        }

        // Use compute to atomically check and modify lobby state
        boolean[] success = { false };
        lobbies.compute(lobbyId, (id, lobby) -> {
            if (lobby == null) {
                return null; // Lobby doesn't exist
            }
            if (!lobby.checkPassword(password)) {
                return lobby; // Wrong password, don't modify
            }
            if (!lobby.addPlayer(userId)) {
                return lobby; // Lobby full or other error
            }
            success[0] = true;
            return lobby;
        });

        if (success[0]) {
            userToLobby.put(userId, lobbyId);
            logger.info("User {} joined lobby {}", userId, lobbyId);
        }
        return success[0];
    }

    /**
     * Leaves a lobby atomically.
     */
    public void leaveLobby(String userId) {
        String lobbyId = userToLobby.remove(userId);
        if (lobbyId != null) {
            // Use compute for atomic check-and-remove
            lobbies.compute(lobbyId, (id, lobby) -> {
                if (lobby == null) {
                    return null;
                }
                lobby.removePlayer(userId);
                // Remove lobby if empty or host left
                if (lobby.getPlayerCount() == 0 || userId.equals(lobby.getHostId())) {
                    logger.info("Lobby {} removed (empty or host left)", lobbyId);
                    return null; // Remove from map
                }
                return lobby;
            });
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
     * Starts a lobby game atomically.
     */
    public boolean startLobby(String lobbyId, String userId) {
        boolean[] success = { false };
        lobbies.compute(lobbyId, (id, lobby) -> {
            if (lobby == null) {
                return null;
            }
            if (!lobby.getHostId().equals(userId)) {
                return lobby; // Only host can start
            }
            if (!lobby.canStart()) {
                return lobby; // Cannot start yet
            }
            lobby.start();
            success[0] = true;
            logger.info("Lobby {} started", lobbyId);
            return lobby;
        });
        return success[0];
    }

    /**
     * Gets the total number of active lobbies.
     */
    public int getLobbyCount() {
        return lobbies.size();
    }

    /**
     * Clears all lobbies (for testing).
     */
    public void clearAll() {
        lobbies.clear();
        userToLobby.clear();
        logger.info("All lobbies cleared");
    }
}
