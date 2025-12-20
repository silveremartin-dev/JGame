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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GooseRules extends AbstractRuleset {

    // https://www.supercoloring.com/paper-crafts/vintage-board-game-of-the-goose
    // Rules of the game :

    // This game uses two dice. Each player must have a separate mark to clearly mark the place where the points obtained have brought him. The players set the fines to be paid for the various accidents and place a bet.
    //  Each throws the dice in turn, then counts, on the board, as many squares as points brought and places his mark on the last of these points.
    // The first to reach number 63 wins the game and collects the amount of stakes and fines. If you exceed this number, you double the point by retracing your steps.
    // Any player whose dice land on one of the geese placed 9 by 9, doubles his point until he encounters no more.
    // If from the first throw of the dice, we roll 9, by 6 and 3 we will place ourselves at n° 26, or by 5 and 4 at 53. If from the first throw of the dice, we bring 6, where there is a bridge, we will be placed at number 12.
    // Accidents – Whoever gets to No. 19, Inn, pays the fine and stays there until the other players have each played twice.
    // Whoever arrives at number 31, where there is a well, pays the fine and stays there until another player, arriving at the same number, takes him out: he then takes the place that this player just quit.
    // Whoever arrives at No. 42, labyrinth, pays the fine and returns to No. 30.
    // Anyone who arrives at number 52, the prison, pays the fine and stays there until someone else takes him out; he then takes the place occupied by the latter player.
    // Whoever gets to death at number 58 pays the fine and restarts the game.
    // Any player met by another pays the fine and takes the place that the latter has just left.

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

    //does nothing, call initGame to actually prepare the game
    public GooseRules() {
    }

    private Graph generateBoard() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        board = AbstractLineBoard.generateBoard(63, AbstractSquareTile.class);
        return board;
    }

    public Graph getBoard() {
        return board;
    }

    //number of players expected between 2 to 10
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
                }xxxx
                currentPlayer.setType(AbstractPlayer.BIOLOGICAL);
                currentPlayer.setId();xxxx
                currentPlayer.setState(PlayerInterface.START_STATE);
                currentPlayer.setScore(new ValueScore(1));
                currentPlayer.setPosition(board.getNodes().get(i).);xxxx
                resultPlayers.add(currentPlayer);
            }
            players = resultPlayers;
            return resultPlayers;
        } else throw new IllegalArgumentException("Number of players expected to be between 2 and 10.");
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

    public void initGame(int numPlayers) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        generateBoard();
        generatePlayers(numPlayers);
        getPlayOrder();
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

    //TODO
    public void nextTurn () {
        AbstractPlayer currentPlayer;
        currentPlayer = players.get(turnIndex);
        if (inGameState[turnIndex]==CAN_MOVE) {
            gamenewPostiion = player.position + rollTwoDices();
            switch(newPosition) {
                if otherplayer.position = newPosition otherPlayer.position = oldPosition
                case 9 : if player.firstturn : if firstdice = 5 || firstdice = 4 newPosition = 53 else newPosition = 26
                case 6 : if player.firstturn newPosition = 12; break;
                case 19 : player.state = LOSE_ONE_TURN; break;
                case 31: player.state = NO_MOVE; if other.player.position =31 otherplayer.state = canmove; break;
                case 52: player.state = NO_MOVE; if other.player.position =52 otherplayer.state = canmove; break;
                case 42 : newpostion = 30
                        case58: newPosition = 1
            case (9,18,27,36,45, 54): rollTwo6Dices(); break;
                case 63: player.STATE = WINNER; endGame();break;
        case > 63:
                    newPosition = 63
        } else {
                if (inGameState[turnIndex]==LOSE_ONE_TURN) {
                    inGameState[turnIndex]==CAN_MOVE;
                }
            }
        player.setScore(player.getScore().setScoreValue(player.getPosition()));
        turnIndex++;
        if (turnIndex == players.size()) {
            turnIndex = 0;
            turnNumber++;
        }
    }

    public move(PlayerInterface player, oldPosition newPosition) {
        playerPiece.display()
    }

    public void endGame() {
        AbstractPlayer currentPlayer;
        for (int i = 0; i < players.size(); i++) {
            currentPlayer = players.get(i);
            if (currentPlayer.getState()!=PlayerInterface.HAS_QUIT_STATE) {
                currentPlayer.setState(PlayerInterface.END_STATE);
            }
        }
    }

    //return ths players index from the best to the last
    //duplicate index means equivalent score
    //last may mean : did not attend, did not finish, lowest score
    public int[] getPlayersRank() {
        double[] playersScore;
        playersScore = new double[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playersScore[i] = ((Double)players.get(i).getScore().getScoreValue()).doubleValue();
        }
        return getRanksArray(playersScore);
    }

    //https://stackoverflow.com/questions/36313032/how-to-rank-an-array-according-to-values-of-the-array-in-java
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
