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

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;

public class Gameplay {

    private final GameInterface game;
    private Set<PlayerInterface> players;

    private final List<ActionInterface> actions;

    public Gameplay(@NotNull GameInterface game) {
        this.game = game;
        this.actions = new ArrayList<>();
    }

    public GameInterface getGame() {
        return game;
    }

    //gather players, initialize stuff
    public void setupGameplay() {
        //TODO
    }

    public Set<PlayerInterface> getPlayers() {
        return players;
    }

    //should be done before actually calling startGameplay though you should maybe able to add or remove players during gameplay
    public void setPlayers(@NotNull Set<PlayerInterface> players) {
        this.players = players;
    }

    public void addPlayer(@NotNull PlayerInterface player) {
        players.add(player);
    }

    public void removePlayer(@NotNull PlayerInterface player) {
        players.remove(player);
    }

    public void startGameplay() {
        //TODO
    }

    public void pauseGameplay() {
        //TODO
    }

    public void endGameplay() {
        //TODO
    }

    public boolean isFinished() {
        //TODO
        return false;
    }

    //who's next to play, may be more than one player at a time for realtime games
    public Set<PlayerInterface> getNextTurn() {
        //TODO
        return null;
    }

    public Set<ActionInterface> getLegalActions(PlayerInterface player) {
        //TODO
        return null;
    }

    public boolean isLegaAction(PlayerInterface player, ActionInterface actionInterface) {
        //TODO
        return false;
    }

    public void triggerAction(PlayerInterface player, ActionInterface action) {
        //TODO
    }

    //undo last action
    //action must be in history of actions
    //for some realtime games, this may actually prove to be impossible
    public void undoAction() {
        //TODO
    }

    //redo action that was undone
    public void redoAction() {
        //TODO
    }

    public List<ActionInterface> getActionsHistory() {
        //TODO
        return null;
    }

    //whether the game is finished or not
    public NavigableMap<PlayerInterface, ScoreInterface> getRanks() {
        //TODO
        return null;
    }

    //we could have some additional methods here:
    // to compute game duration from start to end without pause, or by player
    // to get the turn number though turn games may actually have different number of turns for different players playing the same game ("play twice", pass your turn" though the game itself may actually have a global turn computed)

}
