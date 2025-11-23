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
                currentPlayer.setId("player_" + i);
                currentPlayer.setState(PlayerInterface.START_STATE);
                currentPlayer.setScore(new DoubleScore(1)); // Start at position 1
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

    /**
     * Gets the current position of a player.
     * 
     * @param playerIndex the index of the player
     * @return the player's position (1-63)
     */
    public int getPlayerPosition(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < playerPositions.length) {
            return playerPositions[playerIndex];
        }
        return 1; // Default to start
    }

    public void initGame(int numPlayers)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        generateBoard();
        generatePlayers(numPlayers);
        generatePlayOrder();
        generateInGameState();

        // Initialize player positions (all start at 1)
        playerPositions = new int[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            playerPositions[i] = 1;
        }
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
     * Handles dice rolling, movement, special squares, and win conditions.
     */
    public void nextTurn() {
        // Check if game is already won
        if (inGameState[turnIndex] == WINNER) {
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

    /**
     * Handles special rules for the first turn.
     * - Bridge (6): Jump to 12
     * - Roll 9 with 6+3: Jump to 26
     * - Roll 9 with 5+4: Jump to 53
     */
    private int handleFirstTurnSpecialRules(int position, List<Integer> diceRoll) {
        int d1 = diceRoll.get(0);
        int d2 = diceRoll.get(1);

        // Bridge special (position 6 on first turn)
        if (position == 6) {
            return 12;
        }

        // Roll 9 on first turn
        if (position == 9) {
            if ((d1 == 6 && d2 == 3) || (d1 == 3 && d2 == 6)) {
                return 26; // Jump to 26
            } else if ((d1 == 5 && d2 == 4) || (d1 == 4 && d2 == 5)) {
                return 53; // Jump to 53
            }
        }

        return position; // No special rule applies
    }

    /**
     * Handles special squares:
     * - 19 (Inn): Skip next turn
     * - 31 (Well): Wait for another player
     * - 42 (Labyrinth): Go back to 30
     * - 52 (Prison): Wait for another player
     * - 58 (Death): Restart at 1
     * - 63 (End): Win!
     * - Geese (9, 18, 27, 36, 45, 54): Move again
     */
    private int handleSpecialSquare(int position, int diceSum, int playerIndex) {
        switch (position) {
            case 19: // Inn - skip next turn
                inGameState[playerIndex] = LOSE_ONE_TURN;
                return position;

            case 31: // Well - wait for release
            case 52: // Prison - wait for release
                inGameState[playerIndex] = NO_MOVE;
                return position;

            case 42: // Labyrinth - go back to 30
                return 30;

            case 58: // Death - restart
                return 1;

            case 63: // Winner!
                inGameState[playerIndex] = WINNER;
                endGame();
                return position;

            default:
                // Check for Goose squares (multiples of 9, except 63)
                return handleGooseSquares(position, diceSum, playerIndex);
        }
    }

    /**
     * Handles landing on Goose squares (9, 18, 27, 36, 45, 54).
     * Player moves again by the same dice amount.
     */
    private int handleGooseSquares(int position, int diceSum, int playerIndex) {
        // Geese are at 9, 18, 27, 36, 45, 54 (multiples of 9, excluding 63)
        if (position % 9 == 0 && position < 63 && position > 0) {
            int newPos = position + diceSum; // Move again by same amount

            // Handle overshoot
            if (newPos > 63) {
                newPos = 63 - (newPos - 63);
            }

            // Recursively handle if land on another goose or special square
            return handleSpecialSquare(newPos, diceSum, playerIndex);
        }

        return position; // No goose here
    }

    /**
     * Release mechanism for Well (31) and Prison (52).
     * When a player lands on well/prison where another player is stuck,
     * the stuck player is released and swaps positions.
     */
    private void checkReleaseFromWellOrPrison(int landingPosition, int currentPlayerIndex, int oldPosition) {
        for (int i = 0; i < players.size(); i++) {
            if (i != currentPlayerIndex) {
                // If another player is stuck at this position
                if (playerPositions[i] == landingPosition && inGameState[i] == NO_MOVE) {
                    // Release the stuck player - they take current player's old position
                    inGameState[i] = CAN_MOVE;
                    playerPositions[i] = oldPosition;
                    players.get(i).setScore(new DoubleScore(oldPosition));

                    // Current player gets stuck at well/prison
                    inGameState[currentPlayerIndex] = NO_MOVE;
                    break; // Only one player can be released at a time
                }
            }
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
