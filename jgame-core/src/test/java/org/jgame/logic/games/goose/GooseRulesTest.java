/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 *
 * Enhanced with AI assistance from Google Gemini (Antigravity)
 */

package org.jgame.logic.games.goose;

import org.jgame.model.GameUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for GooseRules class.
 */
class GooseRulesTest {

    private GooseRules rules;

    @BeforeEach
    void setUp() {
        rules = new GooseRules();
    }

    @Nested
    @DisplayName("Game Initialization Tests")
    class InitializationTests {

        @Test
        @DisplayName("Game should not be finished at start")
        void gameNotFinishedAtStart() {
            assertFalse(rules.isFinished());
        }

        @Test
        @DisplayName("Winner should be null at start")
        void noWinnerAtStart() {
            assertNull(rules.getWinner());
        }

        @Test
        @DisplayName("Rules instance can be created")
        void rulesCanBeCreated() {
            assertNotNull(rules);
        }
    }

    @Nested
    @DisplayName("Player Management Tests")
    class PlayerManagementTests {

        @Test
        @DisplayName("Can add players")
        void canAddPlayers() {
            GameUser player1 = new GameUser("Player1", "pass1");
            GameUser player2 = new GameUser("Player2", "pass2");

            rules.addPlayer(player1);
            rules.addPlayer(player2);

            List<GameUser> players = rules.getPlayers();
            assertEquals(2, players.size());
        }

        @Test
        @DisplayName("Abstract players can be retrieved")
        void abstractPlayersCanBeRetrieved() {
            GameUser player = new GameUser("Player1", "pass1");
            rules.addPlayer(player);

            assertNotNull(rules.getAbstractPlayers());
            assertFalse(rules.getAbstractPlayers().isEmpty());
        }
    }

    @Nested
    @DisplayName("Game Flow Tests")
    class GameFlowTests {

        @Test
        @DisplayName("Game can be started with enough players")
        void gameCanBeStarted() throws Exception {
            GameUser player1 = new GameUser("Player1", "pass1");
            GameUser player2 = new GameUser("Player2", "pass2");

            rules.addPlayer(player1);
            rules.addPlayer(player2);
            rules.initGame(2);
            rules.startGame();

            // After starting, play order should be generated
            assertNotNull(rules.getPlayOrder());
        }

        @Test
        @DisplayName("Turn number starts at 0")
        void turnNumberStartsAtZero() {
            assertEquals(0, rules.getTurnNumber());
        }

        @Test
        @DisplayName("Turn index starts at 0")
        void turnIndexStartsAtZero() {
            assertEquals(0, rules.getTurnIndex());
        }

        @Test
        @DisplayName("Current turn returns player when game started")
        void currentTurnReturnsPlayer() throws Exception {
            GameUser player1 = new GameUser("Player1", "pass1");
            GameUser player2 = new GameUser("Player2", "pass2");

            rules.addPlayer(player1);
            rules.addPlayer(player2);
            rules.initGame(2);
            rules.startGame();

            // Current turn should return a non-null player
            assertNotNull(rules.getCurrentTurn());
        }
    }

    @Nested
    @DisplayName("Dice Roll Tests")
    class DiceRollTests {

        @Test
        @DisplayName("Dice roll returns list with two values")
        void diceRollReturnsTwoValues() {
            List<Integer> roll = rules.rollTwo6Dices();
            assertNotNull(roll);
            assertEquals(2, roll.size());
        }

        @Test
        @DisplayName("Dice values are between 1 and 6")
        void diceValuesInRange() {
            for (int i = 0; i < 100; i++) {
                List<Integer> roll = rules.rollTwo6Dices();
                for (int value : roll) {
                    assertTrue(value >= 1 && value <= 6,
                            "Dice value should be between 1 and 6, got: " + value);
                }
            }
        }
    }

    @Nested
    @DisplayName("Game State Tests")
    class GameStateTests {

        @Test
        @DisplayName("In-game state can be retrieved")
        void inGameStateCanBeRetrieved() throws Exception {
            GameUser player1 = new GameUser("Player1", "pass1");
            GameUser player2 = new GameUser("Player2", "pass2");

            rules.addPlayer(player1);
            rules.addPlayer(player2);
            rules.initGame(2);

            int[] inGameState = rules.getInGameState();
            assertNotNull(inGameState);
        }

        @Test
        @DisplayName("Player position can be retrieved")
        void playerPositionCanBeRetrieved() throws Exception {
            GameUser player1 = new GameUser("Player1", "pass1");
            GameUser player2 = new GameUser("Player2", "pass2");

            rules.addPlayer(player1);
            rules.addPlayer(player2);
            rules.initGame(2);

            // Player position should be valid (1-indexed in this game)
            int position = rules.getPlayerPosition(0);
            assertTrue(position >= 0, "Position should be non-negative");
        }
    }

    @Nested
    @DisplayName("Constants Tests")
    class ConstantsTests {

        @Test
        @DisplayName("CAN_MOVE constant is defined")
        void canMoveConstant() {
            assertEquals(2, GooseRules.CAN_MOVE);
        }

        @Test
        @DisplayName("LOSE_ONE_TURN constant is defined")
        void loseOneTurnConstant() {
            assertEquals(4, GooseRules.LOSE_ONE_TURN);
        }

        @Test
        @DisplayName("NO_MOVE constant is defined")
        void noMoveConstant() {
            assertEquals(8, GooseRules.NO_MOVE);
        }

        @Test
        @DisplayName("WINNER constant is defined")
        void winnerConstant() {
            assertEquals(16, GooseRules.WINNER);
        }
    }
}
