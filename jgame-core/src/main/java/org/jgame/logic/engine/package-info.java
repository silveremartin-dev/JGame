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
/**
 * Game engine components for AI and strategy evaluation.
 * 
 * <p>
 * This package provides core game engine components for implementing AI
 * players,
 * evaluating game states, and executing strategic decisions. It includes
 * support
 * for minimax algorithms, alpha-beta pruning, and custom heuristics.
 * </p>
 * 
 * <h2>Core Components</h2>
 * <ul>
 * <li>{@link org.jgame.logic.engine.GameState} - Represents a game state
 * snapshot</li>
 * <li>{@link org.jgame.logic.engine.GameAction} - Represents a possible game
 * action</li>
 * <li>{@link org.jgame.logic.engine.Heuristic} - Evaluation function for game
 * states</li>
 * <li>{@link org.jgame.logic.engine.Strategy} - AI strategy implementation</li>
 * </ul>
 * 
 * <h2>AI Integration</h2>
 * <p>
 * This package works closely with {@link org.jgame.ai} to provide AI
 * capabilities.
 * Game states are evaluated using heuristics, and strategies determine the best
 * actions to take.
 * </p>
 * 
 * @since 1.0
 * @version 2.0
 * @see org.jgame.ai
 * @see org.jgame.logic.GameInterface
 */
package org.jgame.logic.engine;