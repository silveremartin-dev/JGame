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
 * Represents a chess move.
 * 
 * <p>
 * Using Java 21 record for immutable move representation.
 * </p>
 * 
 * @param fromRow        source row (0-7)
 * @param fromCol        source column (0-7)
 * @param toRow          destination row (0-7)
 * @param toCol          destination column (0-7)
 * @param promotionPiece piece to promote to (null if not a promotion)
 * @param isCastling     true if this is a castling move
 * @param isEnPassant    true if this is an en passant capture
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public record ChessMove(
        int fromRow,
        int fromCol,
        int toRow,
        int toCol,
        Class<? extends ChessPiece> promotionPiece,
        boolean isCastling,
        boolean isEnPassant) {
    /**
     * Compact constructor with validation.
     */
    public ChessMove {
        if (fromRow < 0 || fromRow > 7 || fromCol < 0 || fromCol > 7) {
            throw new IllegalArgumentException("Invalid source position");
        }
        if (toRow < 0 || toRow > 7 || toCol < 0 || toCol > 7) {
            throw new IllegalArgumentException("Invalid destination position");
        }
    }

    /**
     * Creates a simple move without special flags.
     */
    public ChessMove(int fromRow, int fromCol, int toRow, int toCol) {
        this(fromRow, fromCol, toRow, toCol, null, false, false);
    }

    /**
     * Creates a promotion move.
     */
    public static ChessMove promotion(int fromRow, int fromCol, int toRow, int toCol,
            Class<? extends ChessPiece> promotionPiece) {
        return new ChessMove(fromRow, fromCol, toRow, toCol, promotionPiece, false, false);
    }

    /**
     * Creates a castling move.
     */
    public static ChessMove castling(int fromRow, int fromCol, int toRow, int toCol) {
        return new ChessMove(fromRow, fromCol, toRow, toCol, null, true, false);
    }

    /**
     * Creates an en passant move.
     */
    public static ChessMove enPassant(int fromRow, int fromCol, int toRow, int toCol) {
        return new ChessMove(fromRow, fromCol, toRow, toCol, null, false, true);
    }

    /**
     * Gets algebraic notation for this move (e.g., "e2e4").
     */
    public String toAlgebraic() {
        char fromFile = (char) ('a' + fromCol);
        char toFile = (char) ('a' + toCol);
        int fromRank = 8 - fromRow;
        int toRank = 8 - toRow;
        return String.format("%c%d%c%d", fromFile, fromRank, toFile, toRank);
    }

    /**
     * Checks if this is a promotion move.
     */
    public boolean isPromotion() {
        return promotionPiece != null;
    }
}
