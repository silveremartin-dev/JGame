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
package org.jgame.server.api;

import io.javalin.http.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgame.model.GameScore;
import org.jgame.server.persistence.dao.UserGameStatsDAO;
import org.jgame.plugin.GameDescriptor;

import java.util.List;
import java.util.Map;

/**
 * REST API controller for game operations.
 *
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class GameApiController {

        private static final Logger logger = LogManager.getLogger(GameApiController.class);

        private final UserGameStatsDAO statsDAO;

        // Hardcoded game list for now - later will come from PluginLoader
        private static final List<GameDescriptor> AVAILABLE_GAMES = List.of(
                        new GameDescriptor("chess", "Chess", "1.0", "JGame",
                                        "Classic chess game", "Standard chess rules", 2, 2, Map.of()),
                        new GameDescriptor("checkers", "Checkers", "1.0", "JGame",
                                        "Classic checkers game", "Standard checkers rules", 2, 2, Map.of()),
                        new GameDescriptor("goose", "Game of the Goose", "1.0", "JGame",
                                        "Classic dice-based board game", "Roll dice and reach the end", 2, 6, Map.of()),
                        new GameDescriptor("solitaire", "Solitaire", "1.0", "JGame",
                                        "Klondike solitaire", "Classic card game", 1, 1, Map.of()));

        /**
         * Creates a new GameApiController with injected dependencies.
         * 
         * @param statsDAO data access object for game stats
         */
        public GameApiController(UserGameStatsDAO statsDAO) {
                this.statsDAO = statsDAO;
        }

        /**
         * GET /api/games - List all available games
         */
        public void listGames(Context ctx) {
                String sortBy = ctx.queryParam("sort"); // name, rating, downloads
                String search = ctx.queryParam("q");

                List<GameDescriptor> games = AVAILABLE_GAMES;

                // Filter by search
                if (search != null && !search.isBlank()) {
                        String query = search.toLowerCase();
                        games = games.stream()
                                        .filter(g -> g.name().toLowerCase().contains(query)
                                                        || g.description().toLowerCase().contains(query))
                                        .toList();
                }

                // Sort
                if ("name".equals(sortBy)) {
                        games = games.stream()
                                        .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
                                        .toList();
                }

                ctx.json(games);
        }

        /**
         * GET /api/games/{gameId} - Get game details
         */
        public void getGame(Context ctx) {
                String gameId = ctx.pathParam("gameId");

                AVAILABLE_GAMES.stream()
                                .filter(g -> g.id().equals(gameId))
                                .findFirst()
                                .ifPresentOrElse(
                                                ctx::json,
                                                () -> ctx.status(404).json(Map.of("error", "Game not found")));
        }

        /**
         * GET /api/scores/{gameId}/leaderboard - Get game leaderboard
         */
        public void getLeaderboard(Context ctx) {
                String gameId = ctx.pathParam("gameId");
                int limit = ctx.queryParamAsClass("limit", Integer.class).getOrDefault(10);

                try {
                        List<GameScore> leaderboard = statsDAO.getLeaderboard(gameId, limit);
                        ctx.json(leaderboard);
                } catch (Exception e) {
                        logger.error("Failed to get leaderboard for {}", gameId, e);
                        ctx.status(500).json(Map.of("error", "Failed to get leaderboard"));
                }
        }
}
