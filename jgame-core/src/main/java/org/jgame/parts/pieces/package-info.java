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
 * Game piece implementations.
 * 
 * <p>
 * Abstract game piece implementations including movable pieces, cards, tokens,
 * and domino tiles. All pieces implement
 * {@link org.jgame.parts.PieceInterface}.
 * </p>
 * 
 * <h2>Components</h2>
 * <ul>
 * <li>{@link org.jgame.parts.pieces.AbstractMovablePiece} - Base for movable
 * pieces (Chess, Checkers)</li>
 * <li>{@link org.jgame.parts.pieces.AbstractCard} - Base for card pieces</li>
 * </ul>
 * 
 * @since 1.0
 * @version 2.0
 * @see org.jgame.parts.PieceInterface
 */
package org.jgame.parts.pieces;