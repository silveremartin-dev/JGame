/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
 *
 * Enhanced with AI assistance from Google Gemini (Antigravity)
 */


package org.jgame.logic;

import org.jetbrains.annotations.NotNull;
import org.jgame.parts.PlayerInterface;

import java.util.*;

public class ScoreBoard {

    private final Set<Gameplay> scoreboard;

    public ScoreBoard() {
        scoreboard = new HashSet<Gameplay>();
    }

    public Set<Gameplay> getScoreboard() {
        return scoreboard;
    }

    //should mostly not happen
    public void removeGameplay(@NotNull Gameplay gameplay) {
        scoreboard.remove(gameplay);
    }

    public Set<GameInterface> getAllGames() {
        Set<GameInterface> gamesResult;
        Iterator<Gameplay> scoreboardIterator;
        gamesResult = new HashSet<GameInterface>();
        scoreboardIterator = scoreboard.iterator();
        while (scoreboardIterator.hasNext()) {
            gamesResult.add(scoreboardIterator.next().getGame());
        }
        return gamesResult;
    }

    public Set<Gameplay> getAllGameplays(@NotNull GameInterface game) {
        Set<Gameplay> gameplaysResult;
        Iterator<Gameplay> scoreboardIterator;
        Gameplay currentGameplay;
        gameplaysResult = new HashSet<Gameplay>();
        scoreboardIterator = scoreboard.iterator();
        while (scoreboardIterator.hasNext()) {
            currentGameplay = scoreboardIterator.next();
            if (currentGameplay.getGame().equals(game)) {
                gameplaysResult.add(currentGameplay);
            }
        }
        return gameplaysResult;
    }

    public Set<PlayerInterface> getAllPlayers() {
        Set<PlayerInterface> playersResult;
        Iterator<Gameplay> scoreboardIterator;
        playersResult = new HashSet<PlayerInterface>();
        scoreboardIterator = scoreboard.iterator();
        while (scoreboardIterator.hasNext()) {
            playersResult.addAll(scoreboardIterator.next().getPlayers());
        }
        return playersResult;
    }

    //for a given game, the best score by player
    public Map<PlayerInterface, ScoreInterface> getTopScores(@NotNull GameInterface gameInterface) {
        Map<PlayerInterface, ScoreInterface> scoresResult;
        Set<Gameplay> gameplays;
        Iterator<Gameplay> gameplaysIterator;
        Set<PlayerInterface> currentPlayers;
        Iterator<PlayerInterface> playersIterator;
        PlayerInterface currentPlayer;
        scoresResult = new HashMap<>();
        gameplays = getAllGameplays(gameInterface);
        gameplaysIterator = gameplays.iterator();
        while (gameplaysIterator.hasNext()) {
            currentPlayers = gameplaysIterator.next().getPlayers();
            playersIterator = currentPlayers.iterator();
            while (playersIterator.hasNext()) {
                currentPlayer = playersIterator.next();
                if (scoresResult.containsKey(currentPlayer)) {
                    //TODO
                    if (currentPlayer.getScore().compareTo(scoresResult.get(currentPlayer))>0) {
                        scoresResult.replace(currentPlayer, currentPlayer.getScore());
                    }
                } else {
                    scoresResult.put(currentPlayer, currentPlayer.getScore());
                }
            }
        }
        return scoresResult;
    }

    //may be we would also like all scores for a player for a given game
    //or top scores for a player for all games
    //or only scores above some level
}
