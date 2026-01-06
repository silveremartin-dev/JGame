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
 * Game implementations and abstract base classes.
 * 
 * <p>
 * This package provides concrete game implementations and abstract base classes
 * for different game types including board games, card games, puzzle games, and
 * platform games. All games extend {@link org.jgame.logic.games.AbstractGame}
 * and
 * implement {@link org.jgame.logic.GameInterface}.
 * </p>
 * 
 * <h2>Abstract Base Classes</h2>
 * <ul>
 * <li>{@link org.jgame.logic.games.AbstractGame} - Base class for all
 * games</li>
 * <li>{@link org.jgame.logic.games.AbstractBoardGame} - Base for board-based
 * games</li>
 * <li>{@link org.jgame.logic.games.AbstractCardGame} - Base for card games</li>
 * <li>{@link org.jgame.logic.games.AbstractPuzzleGame} - Base for puzzle
 * games</li>
 * <li>{@link org.jgame.logic.games.AbstractPlatformGame} - Base for platform
 * games</li>
 * </ul>
 * 
 * <h2>Concrete Implementations</h2>
 * <p>
 * Concrete game implementations are found in subpackages:
 * </p>
 * <ul>
 * <li>{@code org.jgame.logic.games.chess} - Chess game implementation</li>
 * <li>{@code org.jgame.logic.games.checkers} - Checkers game
 * implementation</li>
 * <li>{@code org.jgame.logic.games.goose} - Game of the Goose
 * implementation</li>
 * <li>{@code org.jgame.logic.games.solitaire} - Solitaire card game</li>
 * </ul>
 * 
 * @since 1.0
 * @version 2.0
 * @see org.jgame.logic.GameInterface
 * @see org.jgame.parts
 */
package org.jgame.logic.games;