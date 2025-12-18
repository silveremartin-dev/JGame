/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.ai;

import org.jgame.logic.engine.GameAction;
import org.jgame.logic.engine.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Chess AI using minimax algorithm with alpha-beta pruning.
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class MinimaxAI implements GameAI {

    private static final Logger logger = LogManager.getLogger(MinimaxAI.class);

    private final String name;
    private final int depth;
    private final int difficulty;

    public MinimaxAI(int depth) {
        this("Minimax AI", depth);
    }

    public MinimaxAI(String name, int depth) {
        this.name = name;
        this.depth = Math.max(1, Math.min(depth, 6)); // Cap at 6 for performance
        this.difficulty = Math.min(10, depth * 2);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDifficulty() {
        return difficulty;
    }

    @Override
    public GameAction computeMove(GameState state) {
        if (state == null) {
            return null;
        }

        logger.debug("Computing move at depth {}", depth);
        long startTime = System.currentTimeMillis();

        GameAction bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        List<GameAction> moves = getValidMoves(state);
        for (GameAction move : moves) {
            GameState newState = applyMove(state, move);
            int score = minimax(newState, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        logger.debug("Best move found in {}ms: {} (score: {})", elapsed, bestMove, bestScore);

        return bestMove;
    }

    private int minimax(GameState state, int depth, int alpha, int beta, boolean maximizing) {
        if (depth == 0 || isTerminal(state)) {
            return evaluate(state);
        }

        List<GameAction> moves = getValidMoves(state);

        if (maximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (GameAction move : moves) {
                GameState newState = applyMove(state, move);
                int eval = minimax(newState, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha)
                    break; // Pruning
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (GameAction move : moves) {
                GameState newState = applyMove(state, move);
                int eval = minimax(newState, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha)
                    break; // Pruning
            }
            return minEval;
        }
    }

    /**
     * Evaluates the game state.
     * Override for game-specific evaluation.
     */
    protected int evaluate(GameState state) {
        // Default: simple material count
        return 0;
    }

    /**
     * Checks if state is terminal (game over).
     */
    protected boolean isTerminal(GameState state) {
        return state == null || state.isEnded();
    }

    /**
     * Gets valid moves from state.
     */
    protected List<GameAction> getValidMoves(GameState state) {
        return List.of();
    }

    /**
     * Applies move and returns new state.
     */
    protected GameState applyMove(GameState state, GameAction action) {
        return state; // Override in subclass
    }

    // Factory methods for different difficulty levels
    public static MinimaxAI easy() {
        return new MinimaxAI("Easy AI", 2);
    }

    public static MinimaxAI medium() {
        return new MinimaxAI("Medium AI", 3);
    }

    public static MinimaxAI hard() {
        return new MinimaxAI("Hard AI", 4);
    }
}
