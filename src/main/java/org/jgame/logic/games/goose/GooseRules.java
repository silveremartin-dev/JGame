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

import org.jgame.logic.AbstractRuleset;
import org.jgame.logic.ActionInterface;
import org.jgame.logic.Gameplay;
import org.jgame.logic.scores.ValueScore;
import org.jgame.parts.PlayerInterface;
import org.jgame.parts.boards.AbstractLineBoard;
import org.jgame.parts.players.AbstractPlayer;
import org.jgame.parts.tiles.AbstractSquareTile;
import org.jgame.util.Graph;
import org.jgame.util.RandomGenerator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GooseRules extends AbstractRuleset {

    // https://www.supercoloring.com/paper-crafts/vintage-board-game-of-the-goose
    // Rules of the game :

    // This game uses two dice. Each player must have a separate mark to clearly
    // mark the place where the points obtained have brought him. The players set
    // the fines to be paid for the various accidents and place a bet.
    // Each throws the dice in turn, then counts, on the board, as many squares as
    // points brought and places his mark on the last of these points.
    // The first to reach number 63 wins the game and collects the amount of stakes
    // and fines. If you exceed this number, you double the point by retracing your
    // steps.
    // Any player whose dice land on one of the geese placed 9 by 9, doubles his
    // point until he encounters no more.
    // If from the first throw of the dice, we roll 9, by 6 and 3 we will place
    // ourselves at n° 26, or by 5 and 4 at 53. If from the first throw of the dice,
    // we bring 6, where there is a bridge, we will be placed at number 12.
    // Accidents – Whoever gets to No. 19, Inn, pays the fine and stays there until
    // the other players have each played twice.
    // Whoever arrives at number 31, where there is a well, pays the fine and stays
    // there until another player, arriving at the same number, takes him out: he
    // then takes the place that this player just quit.
    // Whoever arrives at No. 42, labyrinth, pays the fine and returns to No. 30.
    // Anyone who arrives at number 52, the prison, pays the fine and stays there
    // until someone else takes him out; he then takes the place occupied by the
    // latter player.
    // Whoever gets to death at number 58 pays the fine and restarts the game.
    // Any player met by another pays the fine and takes the place that the latter
    // has just left.

    public static final int CAN_MOVE = 2;
    public static final int LOSE_ONE_TURN = 4;
    public static final int NO_MOVE = 8;
    public static final int WINNER = 16;

    private Graph board;

    private List<AbstractPlayer> players;

    private int[] playOrder;

    private int[] inGameState;

    private int turnNumber;

    private int turnIndex;

    // does nothing, call initGame to actually prepare the game
    public GooseRules() {
    }

    private Graph generateBoard() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        board = AbstractLineBoard.generateBoard(63, AbstractSquareTile.class);
        return board;
    }

    public Graph getBoard() {
        return board;
    }

    // number of players expected between 2 to 10
    private Set<AbstractPlayer> generatePlayers(int numPlayers) {
        if (numPlayers > 1 && numPlayers < 11) {
            Set<AbstractPlayer> resultPlayers;
            AbstractPlayer currentPlayer;
            resultPlayers = new HashSet<>();
            for (int i = 1; i <= numPlayers; i++) {
                currentPlayer = new AbstractPlayer() {
                    @Override
                    public List<ActionInterface> computeNextActions(Gameplay gameplay) {
                        return null;
                    }
                };
                currentPlayer.setType(AbstractPlayer.BIOLOGICAL);
                currentPlayer.setId();
                currentPlayer.setState(PlayerInterface.START_STATE);
                currentPlayer.setScore(new ValueScore(1));
                // TODO: Set initial position properly once board node structure is finalized
                // currentPlayer.setPosition(board.getNodes().get(i).getPosition());
                resultPlayers.add(currentPlayer);
            }
            players = new ArrayList<>(resultPlayers);
            return resultPlayers;
        } else
            throw new IllegalArgumentException("Number of players expected to be between 2 and 10.");
    }

    public List<AbstractPlayer> getPlayers() {
        return players;
    }

    private int[] generatePlayOrder() {
        int[] playTurn;
        playTurn = new int[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playTurn[i] = i + 1;
        }
        int randomIndexToSwap;
        int tempValue;
        Random random;
        random = new Random();
        for (int i = 0; i < playTurn.length; i++) {
            randomIndexToSwap = random.nextInt(playTurn.length);
            tempValue = playTurn[randomIndexToSwap];
            playTurn[randomIndexToSwap] = playTurn[i];
            playTurn[i] = tempValue;
        }
        playOrder = playTurn;
        return playTurn;
    }

    public int[] getPlayOrder() {
        return playOrder;
    }

    private int[] generateInGameState() {
        int[] gameState;
        gameState = new int[players.size()];
        for (int i = 0; i < players.size(); i++) {
            gameState[i] = CAN_MOVE;
        }
        inGameState = gameState;
        return gameState;
    }

    public int[] getInGameState() {
        return inGameState;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public int getTurnIndex() {
        return turnIndex;
    }

    public void initGame(int numPlayers)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        generateBoard();
        generatePlayers(numPlayers);
        generatePlayOrder();
        generateInGameState();
    }

    public void startGame() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setState(PlayerInterface.START_STATE);
        }
        turnNumber = 1;
        turnIndex = 0;
    }

    public void quitGame(AbstractPlayer player) {
        player.setState(PlayerInterface.HAS_QUIT_STATE);
    }

    /**
     * Process the next turn in the game.
     * TODO: Complete implementation of game rules
     * - Handle player collision (player meeting another player)
     * - Implement first turn special rules (positions 6 and 9)
     * - Implement special tiles (inn, well, labyrinth, prison, death, geese)
     * - Handle position overflow (bounce back from 63)
     */
    public void nextTurn() {
        AbstractPlayer currentPlayer;
        currentPlayer = players.get(turnIndex);
        if (inGameState[turnIndex] == CAN_MOVE) {
            // TODO: Implement complete game logic
            // Roll dice and move player
            List<Integer> diceRoll = rollTwo6Dices();
            int diceSum = diceRoll.get(0) + diceRoll.get(1);

            // Placeholder implementation - needs completion
            /*
             * int newPosition = player.position + diceSum;
             * 
             * switch(newPosition) {
             * case 6: // Bridge - first turn only
             * if (player.firstTurn) newPosition = 12;
             * break;
             * case 9: // Goose - first turn special
             * if (player.firstTurn) {
             * if (diceRoll.get(0) == 5 || diceRoll.get(0) == 4) {
             * newPosition = 53;
             * } else {
             * newPosition = 26;
             * }
             * }
             * break;
             * case 19: // Inn
             * inGameState[turnIndex] = LOSE_ONE_TURN;
             * break;
             * case 31: // Well
             * case 52: // Prison
             * inGameState[turnIndex] = NO_MOVE;
             * // TODO: Handle release by another player
             * break;
             * case 42: // Labyrinth
             * newPosition = 30;
             * break;
             * case 58: // Death
             * newPosition = 1;
             * break;
             * case 63: // Winner
             * inGameState[turnIndex] = WINNER;
             * endGame();
             * break;
             * default:
             * if (newPosition > 63) {
             * // Bounce back
             * newPosition = 63 - (newPosition - 63);
             * }
             * // Check for goose tiles (9, 18, 27, 36, 45, 54)
             * if (newPosition % 9 == 0 && newPosition != 63) {
             * // Double the dice roll
             * newPosition += diceSum;
             * }
             * break;
             * }
             */

            // player.setPosition(newPosition);
            // player.setScore(new ValueScore(newPosition));
        } else {
            if (inGameState[turnIndex] == LOSE_ONE_TURN) {
                inGameState[turnIndex] = CAN_MOVE;
            }
        }

        // Move to next player
        turnIndex++;
        if (turnIndex == players.size()) {
            turnIndex = 0;
            turnNumber++;
        }
    }

    /**
     * Move a player piece on the board.
     * TODO: Implement visual representation and animation
     */
    public void move(PlayerInterface player, int oldPosition, int newPosition) {
        // TODO: Implement piece movement and display
        // playerPiece.display();
    }

    public void endGame() {
        AbstractPlayer currentPlayer;
        for (int i = 0; i < players.size(); i++) {
            currentPlayer = players.get(i);
            if (currentPlayer.getState() != PlayerInterface.HAS_QUIT_STATE) {
                currentPlayer.setState(PlayerInterface.END_STATE);
            }
        }
    }

    // return the players index from the best to the last
    // duplicate index means equivalent score
    // last may mean : did not attend, did not finish, lowest score
    public int[] getPlayersRank() {
        double[] playersScore;
        playersScore = new double[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playersScore[i] = ((Double) players.get(i).getScore().getScoreValue()).doubleValue();
        }
        return getRanksArray(playersScore);
    }

    // https://stackoverflow.com/questions/36313032/how-to-rank-an-array-according-to-values-of-the-array-in-java
    private static int[] getRanksArray(double[] array) {
        int[] result;
        int count;
        result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            count = 0;
            for (int j = 0; j < array.length; j++) {
                if (array[j] > array[i]) {
                    count++;
                }
            }
            result[i] = count + 1;
        }
        return result;
    }

    public List<Integer> rollTwo6Dices() {
        return RandomGenerator.rollDices(6, 2);
    }

}
