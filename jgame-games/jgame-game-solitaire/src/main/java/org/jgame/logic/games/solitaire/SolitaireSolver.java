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
package org.jgame.logic.games.solitaire;

import org.jgame.ai.GameAI;
import org.jgame.logic.engine.GameAction;
import org.jgame.logic.engine.GameState;
import java.util.List;

/**
 * AI Solver for Solitaire (Hint system).
 * Prioritizes moves to foundations, then tableau moves that reveal cards.
 */
public class SolitaireSolver implements GameAI {

    @Override
    public String getName() {
        return "Solitaire Solver";
    }

    @Override
    public int getDifficulty() {
        return 5;
    }

    @Override
    public GameAction computeMove(GameState state) {
        if (state == null || state.availableActions().isEmpty()) {
            return null;
        }

        List<GameAction> actions = state.availableActions();

        // 1. Prioritize Foundation moves
        GameAction foundationMove = actions.stream()
                .filter(a -> a.actionType().contains("FOUNDATION"))
                .findFirst()
                .orElse(null);
        if (foundationMove != null)
            return foundationMove;

        // 2. Prioritize Tableau moves
        GameAction tableauMove = actions.stream()
                .filter(a -> a.actionType().contains("TABLEAU"))
                .findFirst()
                .orElse(null);
        if (tableauMove != null)
            return tableauMove;

        // 3. Last resort: Draw
        return actions.stream()
                .filter(a -> a.actionType().equals("DRAW"))
                .findFirst()
                .orElse(actions.get(0));
    }
}
