/*
 * Copyright 2022 Silvere Martin-Michiellot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
