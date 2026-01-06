package org.jgame.logic.games.goose;

import org.jgame.logic.games.AbstractBoardGame;
import org.jgame.logic.scores.IntScore;
import org.jgame.parts.PlayerInterface;
import org.jgame.parts.BoardInterface;
import org.jgame.parts.boards.AbstractLineBoard;
import org.jgame.parts.players.GamePlayer;
import org.jgame.parts.players.AbstractPlayer;
import org.jgame.parts.tiles.AbstractSquareTile;
import org.jgame.model.GameUser;
import org.jgame.util.Graph;
import org.jgame.parts.dice.Die;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GooseRules extends AbstractBoardGame {

    public static final int CAN_MOVE = 2;
    public static final int LOSE_ONE_TURN = 4;
    public static final int NO_MOVE = 8;
    public static final int WINNER = 16;

    // Dice from parts
    private final Die die1 = Die.D6; // Use static D6 as it's standard
    private final Die die2 = Die.D6; // Or new Die(6) if we want separate instances, but D6 is stateless (random
                                     // based)

    private Graph<GooseSquareTile> board;
    // players list is in AbstractGame
    private int[] playOrder;
    private int[] inGameState;
    private int turnNumber;
    private int turnIndex;
    private int[] playerPositions; // Track each player's position (1-63)
    private List<Integer> lastDiceRoll; // Store last dice roll for release mechanism
    private boolean gameStarted = false;
    private boolean gameFinished = false;
    private PlayerInterface winner = null;

    public GooseRules() {
        super("Game of the Goose", "1.0", "A classic race game");
        // players initialized in super
    }

    @Override
    public void addPlayer(org.jgame.parts.PlayerInterface player) {
        if (gameStarted) {
            throw new IllegalStateException("Cannot add players after game has started");
        }
        super.addPlayer(player);
        if (player instanceof AbstractPlayer) {
            ((AbstractPlayer) player).setType(PlayerInterface.BIOLOGICAL);
            ((AbstractPlayer) player).setState(PlayerInterface.START_STATE);
            ((AbstractPlayer) player).setScore(new IntScore(1));
        }
    }

    // Override helper to ensure correct initialization if called with GameUser
    @Override
    public void addPlayer(GameUser user) {
        GamePlayer player = new GamePlayer(user);
        addPlayer(player);
    }

    @Override
    public boolean isFinished() {
        return gameFinished;
    }

    @Override
    public PlayerInterface getWinner() {
        return winner;
    }

    public int getMinPlayers() {
        return 2;
    }

    public int getMaxPlayers() {
        return 6;
    }

    public int getBoardSize() {
        return 63;
    }

    public void startGame() {
        if (getPlayers().size() < 2) {
            throw new IllegalStateException("Need at least 2 players");
        }
        gameStarted = true;
        turnNumber = 1;
        turnIndex = 0;

        // Initialize game state
        try {
            initGame(getPlayers().size());
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize game", e);
        }
    }

    public void endGame() {
        gameFinished = true;
        for (PlayerInterface player : getPlayers()) {
            if (player instanceof AbstractPlayer) {
                ((AbstractPlayer) player).setState(PlayerInterface.END_STATE);
            }
        }
    }

    // --- Original Goose Logic ---

    @SuppressWarnings("unchecked")
    private Graph<GooseSquareTile> generateBoard()
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        board = (Graph<GooseSquareTile>) (Graph<?>) AbstractLineBoard.generateBoard(63,
                (Class<AbstractSquareTile>) (Class<?>) GooseSquareTile.class);
        return board;
    }

    @Override
    public BoardInterface getBoard() {
        // Goose uses Graph-based board via getGraphBoard(); BoardInterface not
        // applicable
        return null;
    }

    public Graph<GooseSquareTile> getGraphBoard() {
        return board;
    }

    private int[] generatePlayOrder() {
        int[] playTurn = new int[getPlayers().size()];
        for (int i = 0; i < getPlayers().size(); i++) {
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
        int[] gameState = new int[getPlayers().size()];
        for (int i = 0; i < getPlayers().size(); i++) {
            gameState[i] = CAN_MOVE;
        }
        inGameState = gameState;
        return gameState;
    }

    public void initGame(int numPlayers)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        generateBoard();
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

        // Safety check for empty players
        if (getPlayers().isEmpty())
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

            // Update position (using IntScore for persistence)
            playerPositions[turnIndex] = newPos;
            PlayerInterface p = getPlayers().get(turnIndex);
            if (p instanceof AbstractPlayer) {
                ((AbstractPlayer) p).setScore(new IntScore(newPos));
            }

        } else if (inGameState[turnIndex] == LOSE_ONE_TURN) {
            // Skip turn, reset to CAN_MOVE
            inGameState[turnIndex] = CAN_MOVE;
        }
        // NO_MOVE players stay stuck until released

        // Move to next player
        turnIndex++;
        if (turnIndex == getPlayers().size()) {
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
                winner = getPlayers().get(playerIndex);
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
        // Need to iterate players, but avoid current
        for (int i = 0; i < getPlayers().size(); i++) {
            if (i != currentPlayerIndex) {
                if (playerPositions[i] == landingPosition && inGameState[i] == NO_MOVE) {
                    inGameState[i] = CAN_MOVE;
                    playerPositions[i] = oldPosition;
                    if (getPlayers().get(i) instanceof AbstractPlayer) {
                        ((AbstractPlayer) getPlayers().get(i)).setScore(new IntScore(oldPosition));
                    }
                    inGameState[currentPlayerIndex] = NO_MOVE;
                    break;
                }
            }
        }
    }

    public List<Integer> rollTwo6Dices() {
        int val1 = die1.roll();
        int val2 = die2.roll();
        List<Integer> rolls = new ArrayList<>();
        rolls.add(val1);
        rolls.add(val2);
        return rolls;
    }

    // Getters for testing
    public int[] getPlayOrder() {
        return playOrder;
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

    public int getPlayerPosition(int playerIndex) {
        if (playerIndex < 0 || playerIndex >= playerPositions.length) {
            return 1; // Default start position
        }
        return playerPositions[playerIndex];
    }

    public String getGameName() {
        return "Game of the Goose";
    }
}
