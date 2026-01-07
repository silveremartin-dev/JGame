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
package org.jgame.parts.players;

import org.jgame.ai.GameAI;
import org.jgame.logic.ActionInterface;
import org.jgame.logic.Gameplay;
import org.jgame.parts.PlayerInterface;
import org.jgame.logic.engine.GameState;
import org.jgame.logic.engine.GameAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

/**
 * Player implementation that uses a GameAI to compute moves.
 */
public class AIPlayer extends AbstractPlayer {

    private static final Logger logger = LogManager.getLogger(AIPlayer.class);
    private final GameAI ai;

    /**
     * Creates a new AI player.
     *
     * @param id player identifier
     * @param ai the AI implementation to use
     */
    public AIPlayer(String id, GameAI ai) {
        setId(id);
        setType(PlayerInterface.ARTIFICIAL);
        this.ai = ai;
        logger.debug("AI Player created with ID: {} and AI: {}", id, ai.getName());
    }

    @Override
    public List<ActionInterface> computeNextActions(Gameplay gameplay) {
        if (ai == null) {
            logger.warn("No AI implementation for player: {}", getId());
            return new ArrayList<>();
        }

        GameState state = gameplay.getGame().toGameState();
        if (state == null) {
            logger.warn("Game {} does not support GameState conversion for AI",
                    gameplay.getGame().getName());

            // Fallback: if AI is RandomAI, we can just use gameplay's legal actions
            if (ai instanceof org.jgame.ai.RandomAI) {
                Set<ActionInterface> legal = gameplay.getLegalActions(this);
                if (!legal.isEmpty()) {
                    List<ActionInterface> list = new ArrayList<>(legal);
                    ActionInterface chosen = list.get(new java.util.Random().nextInt(list.size()));
                    return List.of(chosen);
                }
            }
            return new ArrayList<>();
        }

        GameAction action = ai.computeMove(state);
        if (action == null) {
            logger.debug("AI {} could not find a move", ai.getName());
            return new ArrayList<>();
        }

        // Convert GameAction back to ActionInterface
        // This requires the game to be able to map GameAction -> ActionInterface
        // For now, we return empty or try to find a matching ActionInterface
        return matchAction(gameplay, action);
    }

    private List<ActionInterface> matchAction(Gameplay gameplay, GameAction engineAction) {
        Set<ActionInterface> legalActions = gameplay.getLegalActions(this);

        // Try to find a legal action that matches the engine action
        for (ActionInterface action : legalActions) {
            if (action.toString().equals(engineAction.toString())) {
                return List.of(action);
            }
            // Add more sophisticated matching if needed
        }

        logger.warn("AI chose action {}, but no matching legal ActionInterface found", engineAction);
        return new ArrayList<>();
    }
}
