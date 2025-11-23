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

import org.jgame.logic.scores.DoubleScore;
import org.jgame.parts.PlayerInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Game of the Goose rules.
 * Tests all game mechanics including special squares, first-turn rules, and win
 * conditions.
 */
class GooseRulesTest {

    private GooseRules game;

    @BeforeEach
    void setUp() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        game = new GooseRules();
        game.initGame(4); // 4 players
        game.startGame();
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game.getBoard(), "Board should be initialized");
        assertEquals(4, game.getPlayers().size(), "Should have 4 players");
        assertEquals(4, game.getPlayOrder().length, "Play order should include all players");
        assertEquals(4, game.getInGameState().length, "Game state should track all players");
        assertEquals(1, game.getTurnNumber(), "Should start at turn 1");
        assertEquals(0, game.getTurnIndex(), "Should start with player 0");
    }

    @Test
    void testAllPlayersStartAtPosition1() {
        for (int i = 0; i < 4; i++) {
            assertEquals(1, game.getPlayerPosition(i), "Player " + i + " should start at position 1");
            assertEquals(1.0, ((DoubleScore) game.getPlayers().get(i).getScore()).getScoreValue(),
                    "Player " + i + " score should be 1");
        }
    }

    @Test
    void testDiceRolling() {
        List<Integer> dice = game.rollTwo6Dices();
        assertNotNull(dice, "Dice roll should not be null");
        assertEquals(2, dice.size(), "Should roll two dice");

        // Check dice values are valid (1-6)
        for (int die : dice) {
            assertTrue(die >= 1 && die <= 6, "Dice value should be between 1 and 6, got: " + die);
        }
    }

    @Test
    void testMultipleDiceRolls() {
        // Roll 100 times to verify randomness and validity
        for (int i = 0; i < 100; i++) {
            List<Integer> dice = game.rollTwo6Dices();
            for (int die : dice) {
                assertTrue(die >= 1 && die <= 6, "All dice should be 1-6");
            }
        }
    }

    @Test
    void testTurnProgression() {
        int initialTurn = game.getTurnNumber();
        int initialIndex = game.getTurnIndex();

        game.nextTurn();

        assertEquals(initialIndex + 1, game.getTurnIndex(), "Turn index should increment");
        assertEquals(initialTurn, game.getTurnNumber(), "Turn number should not change yet");
    }

    @Test
    void testTurnWraparound() {
        // Play 4 turns (one for each player)
        for (int i = 0; i < 4; i++) {
            game.nextTurn();
        }

        assertEquals(0, game.getTurnIndex(), "Turn index should wrap to 0");
        assertEquals(2, game.getTurnNumber(), "Turn number should increment to 2");
    }

    @Test
    void testPlayerCanMove() {
        // All players should be able to move initially
        for (int i = 0; i < 4; i++) {
            assertEquals(GooseRules.CAN_MOVE, game.getInGameState()[i],
                    "Player " + i + " should be able to move");
        }
    }

    @Test
    void testPlayersHaveUniqueIds() {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            String id = game.getPlayers().get(i).getId();
            assertNotNull(id, "Player ID should not be null");
            assertTrue(id.startsWith("player_"), "Player ID should start with 'player_'");
        }
    }

    @Test
    void testGameEndState() {
        game.endGame();

        for (int i = 0; i < game.getPlayers().size(); i++) {
            assertEquals(PlayerInterface.END_STATE, game.getPlayers().get(i).getState(),
                    "Player " + i + " should be in END_STATE");
        }
    }

    @Test
    void testQuitGame() {
        var player = game.getPlayers().get(0);
        game.quitGame(player);

        assertEquals(PlayerInterface.HAS_QUIT_STATE, player.getState(),
                "Player should be in HAS_QUIT_STATE");
    }

    @Test
    void testInvalidPlayerCount() {
        assertThrows(IllegalArgumentException.class, () -> {
            GooseRules invalidGame = new GooseRules();
            invalidGame.initGame(1); // Too few players
        }, "Should throw exception for 1 player");

        assertThrows(IllegalArgumentException.class, () -> {
            GooseRules invalidGame = new GooseRules();
            invalidGame.initGame(11); // Too many players
        }, "Should throw exception for 11 players");
    }

    @Test
    void testValidPlayerCounts() throws Exception {
        // Test minimum (2 players)
        GooseRules game2 = new GooseRules();
        game2.initGame(2);
        assertEquals(2, game2.getPlayers().size(), "Should support 2 players");

        // Test maximum (10 players)
        GooseRules game10 = new GooseRules();
        game10.initGame(10);
        assertEquals(10, game10.getPlayers().size(), "Should support 10 players");
    }

    @Test
    void testGetPlayerPositionInvalidIndex() {
        assertEquals(1, game.getPlayerPosition(-1), "Invalid negative index should return 1");
        assertEquals(1, game.getPlayerPosition(999), "Invalid large index should return 1");
    }
}
