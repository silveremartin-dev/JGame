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

package org.jgame.logic.exceptions;

/**
 * Exception thrown when a game state is invalid.
 * 
 * <p>
 * This exception is used to signal invalid game states, such as:
 * </p>
 * <ul>
 * <li>Attempting to play a move when game not started</li>
 * <li>Continuing a game that has already finished</li>
 * <li>Invalid player count</li>
 * <li>Corrupted game state during deserialization</li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class InvalidGameStateException extends GameException {

    /**
     * Constructs a new invalid game state exception with the specified detail
     * message.
     * 
     * @param message the detail message describing the invalid state
     */
    public InvalidGameStateException(String message) {
        super(message);
    }

    /**
     * Constructs a new invalid game state exception with the specified detail
     * message and cause.
     * 
     * @param message the detail message
     * @param cause   the cause of this exception
     */
    public InvalidGameStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
