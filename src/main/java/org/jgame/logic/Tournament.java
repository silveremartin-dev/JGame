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

public class Tournament {

    private final GameInterface game;

    private final Set<PlayerInterface> players;

    private List<Gameplay> matches;

    private Set<Trophy> trophies;

    //set of players should be immutable : Set<PlayerInterface> players = Collections.unmodifiableSet(players);
    //immutable but still (sadly) not unmodifiable: see https://docs.oracle.com/javase/9/core/creating-immutable-lists-sets-and-maps.htm#JSCOR-GUID-DD066F67-9C9B-444E-A3CB-820503735951
    public Tournament(@NotNull GameInterface game, @NotNull Set<PlayerInterface> players) {
        this.game = game;
        this.players = players;
    }

    public Set<PlayerInterface> getPlayers() { return players; }

    public Set<Gameplay> getPlayedMatches() {
        Set<Gameplay> resultMatches;
        resultMatches = new HashSet<Gameplay>();
        for (Gameplay m : matches) {
            if (m.isFinished())
                resultMatches.add(m);
        }
        return resultMatches;
    }

    //search in the list of next matches for a match with player
    public Gameplay getNextMatchForPlayer(@NotNull PlayerInterface player) {
        int i;
        boolean found;
        if (players.contains(player)) {
            i = 0;
            found = false;
            while (i<matches.size() && !found) {
                found = matches.get(i).getPlayers().contains(player);
            }
            if (found) return matches.get(i);
            else return null;
        } else throw new IllegalArgumentException("Tournament doesn't contains this player.");
    }

    public List<Gameplay> getNextMatches() {
        //TODO
        return null;
    }

    //triggers next match from getNextMatches(), and once the gameplay is finished, updates the list and the player ranks
    //eventually, some more gameplays are added to the list as the result of the previous gameplays
    public void playNextMatch(@NotNull Gameplay gameplay) {
        if(matches.contains(gameplay)) {
            gameplay.startGameplay();
        } else throw new IllegalArgumentException("Tournament doesn't contains this gameplay.");
    }

    public boolean isFinished() {
        return getNextMatches().isEmpty();
    }

    //even if the tournament is nt fully finished it may be possible to know the definitive rank for some players and give them trophies as soon as they have finished their matches or left the tournement
    public Set<Trophy> getTrophies() {
        return trophies;
    }

    //the list of trophies shouldn't normally change anymore once the tournament is over
    public void setTrophies(@NotNull Set<Trophy> trophies) {
        this.trophies = trophies;
    }

    //even if the Tournament is not finished
    //used to create and assign trophies
    public Map<PlayerInterface, ScoreInterface> getPlayersScore() {
        Map<PlayerInterface, ScoreInterface> scoresResult;
        Set<PlayerInterface> currentPlayers;
        Iterator<PlayerInterface> playersIterator;
        PlayerInterface currentPlayer;
        scoresResult = new HashMap<>();
        for (Gameplay m : matches) {
            currentPlayers = m.getPlayers();
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

    //from the best to the last, may have equal ranks players
    public NavigableMap<PlayerInterface, ScoreInterface> getPlayersRank() {
        TreeMap<PlayerInterface, ScoreInterface> ranksResult;
        ranksResult = new TreeMap<>();
        ranksResult.putAll(getPlayersScore());
        return ranksResult.descendingMap();
    }

}
