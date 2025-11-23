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

package org.jgame.logic.games.goose;

import org.jgame.logic.ActionInterface;
import org.jgame.logic.Gameplay;
import org.jgame.logic.engine.GameRules;
import org.jgame.logic.scores.DoubleScore;
import org.jgame.parts.PlayerInterface;
import org.jgame.parts.boards.AbstractLineBoard;
import org.jgame.parts.players.AbstractPlayer;
import org.jgame.parts.tiles.AbstractSquareTile;
import org.jgame.server.GameUser;
import org.jgame.util.Graph;
import org.jgame.util.RandomGenerator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GooseRules extends GameRules {

    public static final int CAN_MOVE = 2;
    public static final int LOSE_ONE_TURN = 4;
    public static final int NO_MOVE = 8;
    public static final int WINNER = 16;

    private Graph board;
    private List<AbstractPlayer> players;
    private List<GameUser> gameUsers; // Store original GameUsers
    private int[] playOrder;
    private int[] inGameState;
    private int turnNumber;
    private int turnIndex;
    private int[] playerPositions; // Track each player's position (1-63)
    private List<Integer> lastDiceRoll; // Store last dice roll for release mechanism
    private boolean gameStarted = false;
    private boolean gameFinished = false;
    private GameUser winner = null;

    public GooseRules() {
        this.players = new ArrayList<>();
        this.gameUsers = new ArrayList<>();
    }

    @Override
    public void addPlayer(GameUser player) {
        if (gameStarted) {
            throw new IllegalStateException("Cannot add players after game has started");
        }
        gameUsers.add(player);

        AbstractPlayer abstractPlayer = new AbstractPlayer() {
            @Override
            public List<ActionInterface> computeNextActions(Gameplay gameplay) {
                return null;
            }
        };
        abstractPlayer.setType(AbstractPlayer.BIOLOGICAL);
        abstractPlayer.setId(player.getId() != null ? player.getId().toString() : "unknown");
        abstractPlayer.setState(PlayerInterface.START_STATE);
        abstractPlayer.setScore(new DoubleScore(1));
        players.add(abstractPlayer);
    }

    @Override
    public List<GameUser> getPlayers() {
        return new ArrayList<>(gameUsers);
    }

    public List<AbstractPlayer> getAbstractPlayers() {
        return players;
    }

    @Override
    public boolean isFinished() {
        return gameFinished;
    }

    @Override
    public GameUser getWinner() {
        return winner;
    }

    public String getCurrentTurn() {
        if (players.isEmpty())
            return "No players";
        return "Player " + (turnIndex + 1);
    }

    public void startGame() {
        if (players.size() < 2) {
            throw new IllegalStateException("Need at least 2 players");
        }
        gameStarted = true;
        turnNumber = 1;
        turnIndex = 0;

        // Initialize game state
        try {
            initGame(players.size());
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize game", e);
        }
    }

    // --- Original Goose Logic ---

    @SuppressWarnings("unchecked")
    private Graph generateBoard() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        board = AbstractLineBoard.generateBoard(63, (Class<AbstractSquareTile>) (Class<?>) GooseSquareTile.class);
        return board;
    }

    public Graph getBoard() {
        return board;
    }

    private int[] generatePlayOrder() {
        int[] playTurn = new int[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playTurn[i] = i + 1;
        }
        // Shuffle
        Random random = new Random();
        for (int i = 0; i < playTurn.length; i++) {
            int randomIndexToSwap = random.nextInt(playTurn.length);
            int tempValue = playTurn[randomIndexToSwap];
            playTurn[randomIndexToSwap] = playTurn[i];
            playTurn[i] = tempValue;
        }
        playOrder = playTurn;
        return playTurn;
    }

    private int[] generateInGameState() {
        int[] gameState = new int[players.size()];
        for (int i = 0; i < players.size(); i++) {
            gameState[i] = CAN_MOVE;
        }
        inGameState = gameState;
        return gameState;
    }

    public void initGame(int numPlayers)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        generateBoard();
        // generatePlayers is handled by addPlayer now
        generatePlayOrder();
        generateInGameState();

        // Initialize player positions (all start at 1)
        playerPositions = new int[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            playerPositions[i] = 1;
        }
    }

    public void nextTurn() {
        if (gameFinished)
            return;

        if (inGameState[turnIndex] == WINNER) {
            gameFinished = true;
            return;
        }

        if (inGameState[turnIndex] == CAN_MOVE) {
            // Roll dice
            lastDiceRoll = rollTwo6Dices();
            int diceSum = lastDiceRoll.get(0) + lastDiceRoll.get(1);

            int currentPos = playerPositions[turnIndex];
            int newPos = currentPos + diceSum;

            // Handle first turn special rules
            if (turnNumber == 1 && currentPos == 1) {
                newPos = handleFirstTurnSpecialRules(newPos, lastDiceRoll);
            }

            // Handle overshoot (bounce back from 63)
            if (newPos > 63) {
                newPos = 63 - (newPos - 63);
            }

            // Handle special squares and geese
            newPos = handleSpecialSquare(newPos, diceSum, turnIndex);

            // Check for release mechanism (well/prison)
            if (newPos == 31 || newPos == 52) {
                checkReleaseFromWellOrPrison(newPos, turnIndex, currentPos);
            }

            // Update position
            playerPositions[turnIndex] = newPos;
            players.get(turnIndex).setScore(new DoubleScore(newPos));

        } else if (inGameState[turnIndex] == LOSE_ONE_TURN) {
            // Skip turn, reset to CAN_MOVE
            inGameState[turnIndex] = CAN_MOVE;
        }
        // NO_MOVE players stay stuck until released

        // Move to next player
        turnIndex++;
        if (turnIndex == players.size()) {
            turnIndex = 0;
            turnNumber++;
        }
    }

    private int handleFirstTurnSpecialRules(int position, List<Integer> diceRoll) {
        int d1 = diceRoll.get(0);
        int d2 = diceRoll.get(1);

        if (position == 6)
            return 12; // Bridge

        if (position == 9) {
            if ((d1 == 6 && d2 == 3) || (d1 == 3 && d2 == 6))
                return 26;
            else if ((d1 == 5 && d2 == 4) || (d1 == 4 && d2 == 5))
                return 53;
        }
        return position;
    }

    private int handleSpecialSquare(int position, int diceSum, int playerIndex) {
        switch (position) {
            case 19: // Inn
                inGameState[playerIndex] = LOSE_ONE_TURN;
                return position;
            case 31: // Well
            case 52: // Prison
                inGameState[playerIndex] = NO_MOVE;
                return position;
            case 42: // Labyrinth
                return 30;
            case 58: // Death
                return 1;
            case 63: // Winner
                inGameState[playerIndex] = WINNER;
                gameFinished = true;
                // winner = players.get(playerIndex); // Need mapping to GameUser
                return position;
            default:
                return handleGooseSquares(position, diceSum, playerIndex);
        }
    }

    private int handleGooseSquares(int position, int diceSum, int playerIndex) {
        if (position % 9 == 0 && position < 63 && position > 0) {
            int newPos = position + diceSum;
            if (newPos > 63)
                newPos = 63 - (newPos - 63);
            return handleSpecialSquare(newPos, diceSum, playerIndex);
        }
        return position;
    }

    private void checkReleaseFromWellOrPrison(int landingPosition, int currentPlayerIndex, int oldPosition) {
        for (int i = 0; i < players.size(); i++) {
            if (i != currentPlayerIndex) {
                if (playerPositions[i] == landingPosition && inGameState[i] == NO_MOVE) {
                    inGameState[i] = CAN_MOVE;
                    playerPositions[i] = oldPosition;
                    players.get(i).setScore(new DoubleScore(oldPosition));
                    inGameState[currentPlayerIndex] = NO_MOVE;
                    break;
                }
            }
        }
    }

    public List<Integer> rollTwo6Dices() {
        return RandomGenerator.rollDices(6, 2);
    }
}
