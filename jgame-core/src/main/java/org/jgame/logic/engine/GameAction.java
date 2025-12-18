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
 */

package org.jgame.logic.engine;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

/**
 * Represents a player action in a game.
 *
 * <p>
 * Actions are the fundamental unit of interaction with a game.
 * They are serializable for transmission and storage.
 * </p>
 *
 * @param actionId   unique action identifier
 * @param playerId   the player performing the action
 * @param actionType type of action (game-specific, e.g., "MOVE", "DRAW",
 *                   "PASS")
 * @param parameters action parameters (game-specific)
 * @param timestamp  when the action was performed
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public record GameAction(
        String actionId,
        String playerId,
        String actionType,
        Map<String, Object> parameters,
        Instant timestamp) implements Serializable {

    /** Action type for moving a piece */
    public static final String TYPE_MOVE = "MOVE";
    /** Action type for passing turn */
    public static final String TYPE_PASS = "PASS";
    /** Action type for drawing a card */
    public static final String TYPE_DRAW = "DRAW";
    /** Action type for rolling dice */
    public static final String TYPE_ROLL = "ROLL";
    /** Action type for surrendering */
    public static final String TYPE_SURRENDER = "SURRENDER";

    /**
     * Compact constructor with validation.
     */
    public GameAction {
        if (actionId == null || actionId.isBlank()) {
            throw new IllegalArgumentException("Action ID cannot be null or blank");
        }
        if (playerId == null || playerId.isBlank()) {
            throw new IllegalArgumentException("Player ID cannot be null or blank");
        }
        if (actionType == null || actionType.isBlank()) {
            throw new IllegalArgumentException("Action type cannot be null or blank");
        }
        parameters = parameters != null ? Map.copyOf(parameters) : Map.of();
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    /**
     * Creates a new action with auto-generated ID.
     *
     * @param playerId   player performing the action
     * @param actionType type of action
     * @param parameters action parameters
     * @return new GameAction
     */
    public static GameAction create(String playerId, String actionType, Map<String, Object> parameters) {
        String id = java.util.UUID.randomUUID().toString();
        return new GameAction(id, playerId, actionType, parameters, Instant.now());
    }

    /**
     * Creates a simple move action.
     *
     * @param playerId player performing the move
     * @param from     source position
     * @param to       destination position
     * @return new move GameAction
     */
    public static GameAction move(String playerId, Object from, Object to) {
        return create(playerId, TYPE_MOVE, Map.of("from", from, "to", to));
    }

    /**
     * Creates a pass action.
     *
     * @param playerId player passing their turn
     * @return new pass GameAction
     */
    public static GameAction pass(String playerId) {
        return create(playerId, TYPE_PASS, Map.of());
    }

    /**
     * Creates a roll dice action.
     *
     * @param playerId player rolling
     * @param result   roll result
     * @return new roll GameAction
     */
    public static GameAction roll(String playerId, int... result) {
        return create(playerId, TYPE_ROLL, Map.of("result", result));
    }

    /**
     * Gets a parameter value.
     *
     * @param key parameter key
     * @param <T> expected type
     * @return parameter value or null
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(String key) {
        return (T) parameters.get(key);
    }
}
