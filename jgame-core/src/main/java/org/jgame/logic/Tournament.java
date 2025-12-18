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

public class Tournament {

    private final Set<PlayerInterface> players;

    private List<Gameplay> matches;

    private Set<Trophy> trophies;

    // set of players should be immutable : Set<PlayerInterface> players =
    // Collections.unmodifiableSet(players);
    // immutable but still (sadly) not unmodifiable: see
    // https://docs.oracle.com/javase/9/core/creating-immutable-lists-sets-and-maps.htm#JSCOR-GUID-DD066F67-9C9B-444E-A3CB-820503735951
    public Tournament(@NotNull GameInterface game, @NotNull Set<PlayerInterface> players) {
        this.players = players;
    }

    public Set<PlayerInterface> getPlayers() {
        return players;
    }

    public Set<Gameplay> getPlayedMatches() {
        Set<Gameplay> resultMatches;
        resultMatches = new HashSet<Gameplay>();
        for (Gameplay m : matches) {
            if (m.isFinished())
                resultMatches.add(m);
        }
        return resultMatches;
    }

    // search in the list of next matches for a match with player
    public Gameplay getNextMatchForPlayer(@NotNull PlayerInterface player) {
        int i;
        boolean found;
        if (players.contains(player)) {
            i = 0;
            found = false;
            while (i < matches.size() && !found) {
                found = matches.get(i).getPlayers().contains(player);
            }
            if (found)
                return matches.get(i);
            else
                return null;
        } else
            throw new IllegalArgumentException("Tournament doesn't contains this player.");
    }

    public List<Gameplay> getNextMatches() {
        List<Gameplay> nextMatches = new ArrayList<>();
        if (matches != null) {
            for (Gameplay m : matches) {
                if (!m.isFinished()) {
                    nextMatches.add(m);
                }
            }
        }
        return nextMatches;
    }

    // triggers next match from getNextMatches(), and once the gameplay is finished,
    // updates the list and the player ranks
    // eventually, some more gameplays are added to the list as the result of the
    // previous gameplays
    public void playNextMatch(@NotNull Gameplay gameplay) {
        if (matches.contains(gameplay)) {
            gameplay.startGameplay();
        } else
            throw new IllegalArgumentException("Tournament doesn't contains this gameplay.");
    }

    public boolean isFinished() {
        return getNextMatches().isEmpty();
    }

    // even if the tournament is nt fully finished it may be possible to know the
    // definitive rank for some players and give them trophies as soon as they have
    // finished their matches or left the tournement
    public Set<Trophy> getTrophies() {
        return trophies;
    }

    // the list of trophies shouldn't normally change anymore once the tournament is
    // over
    public void setTrophies(@NotNull Set<Trophy> trophies) {
        this.trophies = trophies;
    }

    // even if the Tournament is not finished
    // used to create and assign trophies
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
                ScoreInterface playerScore = currentPlayer.getScore();
                if (scoresResult.containsKey(currentPlayer)) {
                    // Merge scores - keep the higher score
                    ScoreInterface existingScore = scoresResult.get(currentPlayer);
                    if (playerScore != null && existingScore != null
                            && playerScore.compareTo(existingScore) > 0) {
                        scoresResult.replace(currentPlayer, playerScore);
                    }
                } else {
                    scoresResult.put(currentPlayer, playerScore);
                }
            }
        }
        return scoresResult;
    }

    // from the best to the last, may have equal ranks players
    public NavigableMap<PlayerInterface, ScoreInterface> getPlayersRank() {
        TreeMap<PlayerInterface, ScoreInterface> ranksResult;
        ranksResult = new TreeMap<>();
        ranksResult.putAll(getPlayersScore());
        return ranksResult.descendingMap();
    }

}
