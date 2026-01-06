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
package org.jgame.logic.games.checkers;

/**
 * Represents a move in checkers from one position to another.
 * Used for both regular moves and captures.
 */
public class CheckersMove {
    private final int fromRow;
    private final int fromCol;
    private final int toRow;
    private final int toCol;
    private final boolean isCapture;

    /**
     * Creates a new checkers move.
     * 
     * @param fromRow starting row
     * @param fromCol starting column
     * @param toRow   destination row
     * @param toCol   destination column
     */
    public CheckersMove(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        // Capture if moving 2 squares
        this.isCapture = Math.abs(toRow - fromRow) == 2;
    }

    public int getFromRow() {
        return fromRow;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getToRow() {
        return toRow;
    }

    public int getToCol() {
        return toCol;
    }

    public boolean isCapture() {
        return isCapture;
    }

    @Override
    public String toString() {
        return "(" + fromRow + "," + fromCol + ")" +
                (isCapture ? " X " : " -> ") +
                "(" + toRow + "," + toCol + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CheckersMove))
            return false;
        CheckersMove other = (CheckersMove) obj;
        return this.fromRow == other.fromRow &&
                this.fromCol == other.fromCol &&
                this.toRow == other.toRow &&
                this.toCol == other.toCol;
    }

    @Override
    public int hashCode() {
        return fromRow * 1000 + fromCol * 100 + toRow * 10 + toCol;
    }
}
