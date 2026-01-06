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

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a game lobby where players wait to start a game.
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class GameLobby {

    private final String id;
    private final String gameId;
    private final String hostId;
    private final String name;
    private final int maxPlayers;
    private final Set<String> players;
    private final Instant createdAt;
    private LobbyState state;
    private String password; // Optional password protection

    public enum LobbyState {
        WAITING, STARTING, IN_PROGRESS, FINISHED
    }

    public GameLobby(String id, String gameId, String hostId, String name, int maxPlayers) {
        this.id = id;
        this.gameId = gameId;
        this.hostId = hostId;
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.players = ConcurrentHashMap.newKeySet();
        this.players.add(hostId);
        this.createdAt = Instant.now();
        this.state = LobbyState.WAITING;
    }

    public String getId() {
        return id;
    }

    public String getGameId() {
        return gameId;
    }

    public String getHostId() {
        return hostId;
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Set<String> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public int getPlayerCount() {
        return players.size();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public LobbyState getState() {
        return state;
    }

    public boolean isPasswordProtected() {
        return password != null && !password.isEmpty();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String input) {
        if (!isPasswordProtected())
            return true;
        return password.equals(input);
    }

    public boolean addPlayer(String userId) {
        if (state != LobbyState.WAITING)
            return false;
        if (players.size() >= maxPlayers)
            return false;
        return players.add(userId);
    }

    public boolean removePlayer(String userId) {
        return players.remove(userId);
    }

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    public boolean canStart() {
        return state == LobbyState.WAITING && players.size() >= 2;
    }

    public void start() {
        if (canStart()) {
            state = LobbyState.STARTING;
        }
    }

    public void setInProgress() {
        state = LobbyState.IN_PROGRESS;
    }

    public void finish() {
        state = LobbyState.FINISHED;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("gameId", gameId);
        map.put("hostId", hostId);
        map.put("name", name);
        map.put("maxPlayers", maxPlayers);
        map.put("playerCount", players.size());
        map.put("players", new ArrayList<>(players));
        map.put("state", state.name());
        map.put("passwordProtected", isPasswordProtected());
        map.put("createdAt", createdAt.toString());
        return map;
    }
}
