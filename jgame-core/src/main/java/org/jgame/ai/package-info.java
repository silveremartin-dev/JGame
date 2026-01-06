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
 * Artificial intelligence implementations for game playing.
 * 
 * <p>
 * AI implementations for game playing including minimax algorithm with
 * alpha-beta pruning, random AI, and base AI interfaces.
 * </p>
 * 
 * <h2>Components</h2>
 * <ul>
 * <li>{@link org.jgame.ai.MinimaxAI} - Minimax algorithm with alpha-beta
 * pruning</li>
 * <li>{@link org.jgame.ai.RandomAI} - Random move selection AI</li>
 * <li>{@link org.jgame.ai.GameAI} - Base AI interface</li>
 * </ul>
 * 
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * GameAI ai = new MinimaxAI(maxDepth);
 * Move bestMove = ai.getBestMove(gameState);
 * }</pre>
 * 
 * @since 1.0
 * @version 2.0
 * @see org.jgame.logic.engine
 * @see org.jgame.parts.players.AbstractAIPlayer
 */
package org.jgame.ai;
