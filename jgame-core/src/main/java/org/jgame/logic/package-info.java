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
 * Core game logic framework for the JGame platform.
 * 
 * <p>
 * This package provides the foundational abstractions and interfaces for
 * implementing
 * game logic, rules, scoring systems, and gameplay mechanics. It serves as the
 * backbone
 * for all game implementations in the JGame ecosystem.
 * </p>
 * 
 * <h2>Key Components</h2>
 * <ul>
 * <li>{@link org.jgame.logic.GameInterface} - Primary interface defining game
 * contract</li>
 * <li>{@link org.jgame.logic.AbstractRuleset} - Base class for game rule
 * implementations</li>
 * <li>{@link org.jgame.logic.ScoreInterface} - Scoring system abstraction</li>
 * <li>{@link org.jgame.logic.Rule} - Individual game rule representation</li>
 * <li>{@link org.jgame.logic.Gameplay} - Gameplay state and flow
 * management</li>
 * </ul>
 * 
 * <h2>Subpackages</h2>
 * <ul>
 * <li>{@link org.jgame.logic.games} - Concrete game implementations</li>
 * <li>{@link org.jgame.logic.scores} - Scoring system implementations</li>
 * <li>{@link org.jgame.logic.engine} - Game engine components</li>
 * <li>{@link org.jgame.logic.exceptions} - Game-specific exceptions</li>
 * </ul>
 * 
 * <h2>Usage Example</h2>
 * 
 * <pre>
 * {
 *     &#64;code
 *     public class MyGame extends AbstractGame implements GameInterface {
 *         &#64;Override
 *         public void initializeGame() {
 *             // Setup game state
 *         }
 * 
 *         @Override
 *         public boolean isFinished() {
 *             // Check win conditions
 *             return false;
 *         }
 *     }
 * }
 * </pre>
 * 
 * @since 1.0
 * @version 2.0
 * @see org.jgame.logic.games
 * @see org.jgame.parts
 */
package org.jgame.logic;