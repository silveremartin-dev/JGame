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
package org.jgame.ai;

import org.jgame.logic.engine.GameAction;
import org.jgame.logic.engine.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

/**
 * Simple AI that makes random moves.
 *
 * <p>
 * Useful for testing and as a baseline.
 * </p>
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class RandomAI implements GameAI {

    private static final Logger logger = LogManager.getLogger(RandomAI.class);
    private final Random random = new Random();
    private final String name;

    public RandomAI() {
        this("Random AI");
    }

    public RandomAI(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDifficulty() {
        return 1; // Easiest
    }

    @Override
    public GameAction computeMove(GameState state) {
        if (state == null) {
            logger.warn("Cannot compute move: null state");
            return null;
        }

        List<GameAction> validMoves = getValidMoves(state);
        if (validMoves.isEmpty()) {
            logger.debug("No valid moves available");
            return null;
        }

        GameAction chosen = validMoves.get(random.nextInt(validMoves.size()));
        logger.debug("Random AI chose: {}", chosen);
        return chosen;
    }

    /**
     * Gets valid moves from game state.
     * Override in game-specific implementations.
     */
    protected List<GameAction> getValidMoves(GameState state) {
        // Default: empty list - subclasses should override
        return List.of();
    }
}
