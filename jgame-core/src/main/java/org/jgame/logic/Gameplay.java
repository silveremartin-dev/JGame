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
package org.jgame.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jgame.parts.PlayerInterface;

import java.util.*;

/**
 * Manages the gameplay lifecycle including player turns, actions, and game
 * state.
 *
 * <p>
 * This class coordinates between players and the game instance, handling
 * action validation, turn management, and history tracking.
 * </p>
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 2.0
 */
public class Gameplay {

    private static final Logger logger = LogManager.getLogger(Gameplay.class);

    private final GameInterface game;
    private Set<PlayerInterface> players;
    private final List<ActionInterface> actions;
    private final Deque<ActionInterface> undoStack;
    private final Deque<ActionInterface> redoStack;

    private GameState state;
    private int currentPlayerIndex;

    /**
     * Game states.
     */
    public enum GameState {
        /** Game not yet started */
        SETUP,
        /** Game is running */
        PLAYING,
        /** Game is paused */
        PAUSED,
        /** Game has ended */
        FINISHED
    }

    /**
     * Creates a new gameplay instance.
     *
     * @param game the game to manage
     */
    public Gameplay(@NotNull GameInterface game) {
        this.game = Objects.requireNonNull(game, "Game cannot be null");
        this.players = new LinkedHashSet<>();
        this.actions = new ArrayList<>();
        this.undoStack = new ArrayDeque<>();
        this.redoStack = new ArrayDeque<>();
        this.state = GameState.SETUP;
        this.currentPlayerIndex = 0;
        logger.debug("Gameplay created for game: {}", game.getClass().getSimpleName());
    }

    /**
     * Gets the underlying game instance.
     *
     * @return the game
     */
    public GameInterface getGame() {
        return game;
    }

    /**
     * Gets the current game state.
     *
     * @return current state
     */
    public GameState getState() {
        return state;
    }

    /**
     * Initializes gameplay, gathering players and preparing the game.
     */
    public void setupGameplay() {
        if (state != GameState.SETUP) {
            logger.warn("Cannot setup gameplay - already in state: {}", state);
            return;
        }

        if (players.isEmpty()) {
            logger.warn("Cannot setup gameplay - no players added");
            return;
        }

        game.initialize();
        currentPlayerIndex = 0;
        actions.clear();
        undoStack.clear();
        redoStack.clear();

        logger.info("Gameplay setup complete with {} players", players.size());
    }

    /**
     * Gets all players in the game.
     *
     * @return set of players
     */
    public Set<PlayerInterface> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    /**
     * Sets the players for this game.
     *
     * @param players the players to set
     */
    public void setPlayers(@NotNull Set<PlayerInterface> players) {
        if (state != GameState.SETUP) {
            logger.warn("Cannot set players during gameplay");
            return;
        }
        this.players = new LinkedHashSet<>(Objects.requireNonNull(players));
        logger.debug("Set {} players", players.size());
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player to add
     */
    public void addPlayer(@NotNull PlayerInterface player) {
        players.add(Objects.requireNonNull(player));
        logger.debug("Added player: {}", player);
    }

    /**
     * Removes a player from the game.
     *
     * @param player the player to remove
     */
    public void removePlayer(@NotNull PlayerInterface player) {
        players.remove(player);
        logger.debug("Removed player: {}", player);
    }

    /**
     * Starts the gameplay.
     */
    public void startGameplay() {
        if (state == GameState.SETUP) {
            setupGameplay();
        }

        if (state == GameState.PAUSED || state == GameState.SETUP) {
            state = GameState.PLAYING;
            logger.info("Gameplay started");
        } else {
            logger.warn("Cannot start - current state: {}", state);
        }
    }

    /**
     * Pauses the gameplay.
     */
    public void pauseGameplay() {
        if (state == GameState.PLAYING) {
            state = GameState.PAUSED;
            logger.info("Gameplay paused");
        }
    }

    /**
     * Ends the gameplay.
     */
    public void endGameplay() {
        state = GameState.FINISHED;
        logger.info("Gameplay ended");
    }

    /**
     * Checks if the game is finished.
     *
     * @return true if finished
     */
    public boolean isFinished() {
        return state == GameState.FINISHED || game.isFinished();
    }

    /**
     * Gets the players whose turn it is next.
     *
     * @return set of players for next turn
     */
    public Set<PlayerInterface> getNextTurn() {
        if (players.isEmpty() || isFinished()) {
            return Collections.emptySet();
        }

        List<PlayerInterface> playerList = new ArrayList<>(players);
        PlayerInterface current = playerList.get(currentPlayerIndex % playerList.size());
        return Collections.singleton(current);
    }

    /**
     * Gets legal actions for a player.
     *
     * @param player the player
     * @return set of legal actions
     */
    public Set<ActionInterface> getLegalActions(PlayerInterface player) {
        if (player == null || isFinished()) {
            return Collections.emptySet();
        }
        return game.getLegalActions(player);
    }

    /**
     * Checks if an action is legal for a player.
     *
     * @param player the player
     * @param action the action to check
     * @return true if legal
     */
    public boolean isLegalAction(PlayerInterface player, ActionInterface action) {
        if (player == null || action == null) {
            return false;
        }
        Set<ActionInterface> legal = getLegalActions(player);
        return legal != null && legal.contains(action);
    }

    /**
     * Triggers an action for a player.
     *
     * @param player the player
     * @param action the action to execute
     */
    public void triggerAction(PlayerInterface player, ActionInterface action) {
        if (!isLegalAction(player, action)) {
            logger.warn("Illegal action attempted by {}: {}", player, action);
            return;
        }

        game.executeAction(player, action);
        actions.add(action);
        undoStack.push(action);
        redoStack.clear();

        // Advance to next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        logger.debug("Action executed: {} by {}", action, player);

        if (game.isFinished()) {
            endGameplay();
        }
    }

    /**
     * Undoes the last action.
     */
    public void undoAction() {
        if (undoStack.isEmpty()) {
            logger.debug("Nothing to undo");
            return;
        }

        ActionInterface action = undoStack.pop();
        game.undoAction(action);
        redoStack.push(action);

        // Go back to previous player
        currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();

        logger.debug("Undid action: {}", action);
    }

    /**
     * Redoes a previously undone action.
     */
    public void redoAction() {
        if (redoStack.isEmpty()) {
            logger.debug("Nothing to redo");
            return;
        }

        ActionInterface action = redoStack.pop();
        List<PlayerInterface> playerList = new ArrayList<>(players);
        PlayerInterface player = playerList.get(currentPlayerIndex);

        game.executeAction(player, action);
        undoStack.push(action);

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        logger.debug("Redid action: {}", action);
    }

    /**
     * Gets the history of all actions.
     *
     * @return list of actions in order
     */
    public List<ActionInterface> getActionsHistory() {
        return Collections.unmodifiableList(actions);
    }

    /**
     * Gets the current player rankings.
     *
     * @return map of players to scores, sorted by rank
     */
    public NavigableMap<PlayerInterface, ScoreInterface> getRanks() {
        NavigableMap<PlayerInterface, ScoreInterface> ranks = new TreeMap<>(
                Comparator.comparingInt(p -> -getPlayerScore(p)));

        for (PlayerInterface player : players) {
            ScoreInterface score = game.getScore(player);
            if (score != null) {
                ranks.put(player, score);
            }
        }

        return ranks;
    }

    private int getPlayerScore(PlayerInterface player) {
        ScoreInterface score = game.getScore(player);
        if (score == null) {
            return 0;
        }
        Object value = score.getScoreValue();
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }
}
