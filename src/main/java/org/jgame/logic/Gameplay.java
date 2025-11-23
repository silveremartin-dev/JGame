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
