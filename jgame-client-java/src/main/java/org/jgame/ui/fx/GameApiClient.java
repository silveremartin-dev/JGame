/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)`n * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.ui.fx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgame.model.GameScore;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * HTTP client for communicating with JGame server REST API.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class GameApiClient {

    private static final Logger logger = LogManager.getLogger(GameApiClient.class);
    private static final Gson gson = new Gson();

    private final HttpClient httpClient;
    private String baseUrl;
    private String authToken;

    public GameApiClient(String serverUrl) {
        this.baseUrl = serverUrl.endsWith("/") ? serverUrl.substring(0, serverUrl.length() - 1) : serverUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Sets the authentication token for protected requests.
     */
    public void setAuthToken(String token) {
        this.authToken = token;
    }

    /**
     * Registers a new user.
     */
    public CompletableFuture<ApiResponse> register(String username, String password, String email) {
        String body = gson.toJson(Map.of(
                "username", username,
                "password", password,
                "email", email != null ? email : ""));

        return post("/api/auth/register", body, false);
    }

    /**
     * Logs in a user.
     */
    public CompletableFuture<LoginResponse> login(String username, String password) {
        String body = gson.toJson(Map.of(
                "username", username,
                "password", password));

        return post("/api/auth/login", body, false)
                .thenApply(response -> {
                    if (response.success) {
                        LoginResponse loginResponse = gson.fromJson(response.body, LoginResponse.class);
                        this.authToken = loginResponse.token;
                        return loginResponse;
                    }
                    return new LoginResponse(null, null, response.error);
                });
    }

    /**
     * Gets list of available games.
     */
    public CompletableFuture<List<GameInfo>> getGames() {
        return get("/api/games")
                .thenApply(response -> {
                    if (response.success) {
                        return gson.fromJson(response.body, new TypeToken<List<GameInfo>>() {
                        }.getType());
                    }
                    return List.of();
                });
    }

    /**
     * Gets user's scores.
     */
    public CompletableFuture<List<GameScore>> getUserScores() {
        return get("/api/user/scores")
                .thenApply(response -> {
                    if (response.success) {
                        return gson.fromJson(response.body, new TypeToken<List<GameScore>>() {
                        }.getType());
                    }
                    return List.of();
                });
    }

    /**
     * Gets leaderboard for a game.
     */
    public CompletableFuture<List<GameScore>> getLeaderboard(String gameId, int limit) {
        return get("/api/scores/" + gameId + "/leaderboard?limit=" + limit)
                .thenApply(response -> {
                    if (response.success) {
                        return gson.fromJson(response.body, new TypeToken<List<GameScore>>() {
                        }.getType());
                    }
                    return List.of();
                });
    }

    private CompletableFuture<ApiResponse> get(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET()
                .header("Content-Type", "application/json");

        if (authToken != null) {
            builder.header("Authorization", "Bearer " + authToken);
        }

        return httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString())
                .thenApply(this::toApiResponse)
                .exceptionally(e -> {
                    logger.error("GET {} failed: {}", path, e.getMessage());
                    return new ApiResponse(false, null, e.getMessage());
                });
    }

    private CompletableFuture<ApiResponse> post(String path, String body, boolean requiresAuth) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("Content-Type", "application/json");

        if (requiresAuth && authToken != null) {
            builder.header("Authorization", "Bearer " + authToken);
        }

        return httpClient.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString())
                .thenApply(this::toApiResponse)
                .exceptionally(e -> {
                    logger.error("POST {} failed: {}", path, e.getMessage());
                    return new ApiResponse(false, null, e.getMessage());
                });
    }

    private ApiResponse toApiResponse(HttpResponse<String> response) {
        boolean success = response.statusCode() >= 200 && response.statusCode() < 300;
        return new ApiResponse(success, response.body(), success ? null : "HTTP " + response.statusCode());
    }

    // Response types
    public record ApiResponse(boolean success, String body, String error) {
    }

    public record LoginResponse(String token, String username, String error) {
        public boolean success() {
            return token != null;
        }
    }

    public record GameInfo(
            String id, String name, String version, String author,
            String description, int minPlayers, int maxPlayers) {
    }
}
