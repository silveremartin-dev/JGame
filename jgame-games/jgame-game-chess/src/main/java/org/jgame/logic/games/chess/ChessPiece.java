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

package org.jgame.logic.games.chess;

/**
 * Chess piece types using sealed interface (Java 17+).
 * 
 * <p>
 * Sealed interfaces restrict which classes can implement them,
 * providing exhaustive pattern matching and type safety.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public sealed interface ChessPiece
        permits Pawn, Knight, Bishop, Rook, Queen, King {

    /**
     * Chess piece color.
     */
    enum Color {
        WHITE, BLACK;

        public Color opposite() {
            return this == WHITE ? BLACK : WHITE;
        }
    }

    /**
     * Gets the color of this piece.
     * 
     * @return piece color
     */
    Color getColor();

    /**
     * Gets the symbol for this piece (for notation).
     * 
     * @return piece symbol (K, Q, R, B, N, P)
     */
    char getSymbol();

    /**
     * Gets the material value of this piece.
     * 
     * @return piece value (pawn=1, knight/bishop=3, rook=5, queen=9, king=0)
     */
    int getValue();
}
