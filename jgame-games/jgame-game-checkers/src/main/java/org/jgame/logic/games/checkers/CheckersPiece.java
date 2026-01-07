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

import org.jgame.parts.PieceInterface;
import java.awt.Image;

/**
 * Represents a single checkers piece on the board.
 * Tracks the piece's owner, position, and king status.
 */
public class CheckersPiece implements PieceInterface {
    private final int player; // 1 or 2
    private boolean isKing; // false = regular piece, true = king
    private int row; // 0-7
    private int col; // 0-7

    /**
     * Creates a new checkers piece.
     * 
     * @param player the owning player (1 or 2)
     * @param row    the initial row position (0-7)
     * @param col    the initial column position (0-7)
     */
    public CheckersPiece(int player, int row, int col) {
        this.player = player;
        this.row = row;
        this.col = col;
        this.isKing = false;
    }

    public int getPlayer() {
        return player;
    }

    public boolean isKing() {
        return isKing;
    }

    public void promoteToKing() {
        this.isKing = true;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        String type = isKing ? "K" : "P";
        return "Player" + player + type + "(" + row + "," + col + ")";
    }

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public Image getImage(Image image) {
        return image;
    }

    public CheckersPiece copy() {
        CheckersPiece copy = new CheckersPiece(this.player, this.row, this.col);
        copy.isKing = this.isKing;
        return copy;
    }
}
