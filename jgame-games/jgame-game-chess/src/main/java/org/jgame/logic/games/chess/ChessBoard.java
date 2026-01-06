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
package org.jgame.logic.games.chess;

import org.jgame.parts.boards.AbstractBoard;
import org.jgame.parts.PieceInterface;
import org.jgame.parts.TileInterface;
import org.jgame.parts.BoardInterface;
import org.jgame.util.Graph;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Chess board representation.
 * 
 * <p>
 * 8x8 board with pieces. Uses null for empty squares.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class ChessBoard extends AbstractBoard {

    private final ChessPiece[][] board = new ChessPiece[8][8];

    /**
     * Creates a new chess board in starting position.
     */
    public ChessBoard() {
        setupStartingPosition();
    }

    /**
     * Sets up the standard starting position.
     */
    private void setupStartingPosition() {
        // Black pieces (top, rows 0-1)
        board[0][0] = new Rook(ChessPiece.Color.BLACK);
        board[0][1] = new Knight(ChessPiece.Color.BLACK);
        board[0][2] = new Bishop(ChessPiece.Color.BLACK);
        board[0][3] = new Queen(ChessPiece.Color.BLACK);
        board[0][4] = new King(ChessPiece.Color.BLACK);
        board[0][5] = new Bishop(ChessPiece.Color.BLACK);
        board[0][6] = new Knight(ChessPiece.Color.BLACK);
        board[0][7] = new Rook(ChessPiece.Color.BLACK);

        for (int col = 0; col < 8; col++) {
            board[1][col] = new Pawn(ChessPiece.Color.BLACK);
        }

        // Empty squares (rows 2-5)
        for (int row = 2; row < 6; row++) {
            Arrays.fill(board[row], null);
        }

        // White pieces (bottom, rows 6-7)
        for (int col = 0; col < 8; col++) {
            board[6][col] = new Pawn(ChessPiece.Color.WHITE);
        }

        board[7][0] = new Rook(ChessPiece.Color.WHITE);
        board[7][1] = new Knight(ChessPiece.Color.WHITE);
        board[7][2] = new Bishop(ChessPiece.Color.WHITE);
        board[7][3] = new Queen(ChessPiece.Color.WHITE);
        board[7][4] = new King(ChessPiece.Color.WHITE);
        board[7][5] = new Bishop(ChessPiece.Color.WHITE);
        board[7][6] = new Knight(ChessPiece.Color.WHITE);
        board[7][7] = new Rook(ChessPiece.Color.WHITE);
    }

    /**
     * Gets the piece at the given position.
     * 
     * @param row row (0-7)
     * @param col column (0-7)
     * @return piece at position, or null if empty
     */
    public ChessPiece getPiece(int row, int col) {
        if (!isValidPosition(row, col)) {
            return null;
        }
        return board[row][col];
    }

    /**
     * Sets a piece at the given position.
     * 
     * @param row   row (0-7)
     * @param col   column (0-7)
     * @param piece piece to place (null for empty)
     */
    public void setPiece(int row, int col, ChessPiece piece) {
        if (isValidPosition(row, col)) {
            board[row][col] = piece;
        }
    }

    /**
     * Checks if a position is on the board.
     * 
     * @param row row
     * @param col column
     * @return true if valid
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    /**
     * Checks if a square is empty.
     * 
     * @param row row
     * @param col column
     * @return true if empty
     */
    public boolean isEmpty(int row, int col) {
        return getPiece(row, col) == null;
    }

    /**
     * Makes a move on the board.
     * 
     * @param move the move to make
     * @return captured piece, or null
     */
    public ChessPiece makeMove(ChessMove move) {
        ChessPiece piece = getPiece(move.fromRow(), move.fromCol());
        ChessPiece captured = getPiece(move.toRow(), move.toCol());

        // Move piece
        setPiece(move.toRow(), move.toCol(), piece);
        setPiece(move.fromRow(), move.fromCol(), null);

        // Handle promotion
        if (move.isPromotion() && piece instanceof Pawn) {
            try {
                ChessPiece promoted = move.promotionPiece()
                        .getDeclaredConstructor(ChessPiece.Color.class)
                        .newInstance(piece.getColor());
                setPiece(move.toRow(), move.toCol(), promoted);
            } catch (Exception e) {
                // Keep pawn if promotion fails
            }
        }

        // Handle en passant
        if (move.isEnPassant() && piece instanceof Pawn) {
            // Remove captured pawn (different row than destination)
            int capturedRow = move.fromRow();
            setPiece(capturedRow, move.toCol(), null);
        }

        // Handle castling
        if (move.isCastling() && piece instanceof King) {
            // Move rook
            boolean kingside = move.toCol() > move.fromCol();
            if (kingside) {
                // Kingside: rook from h to f
                ChessPiece rook = getPiece(move.fromRow(), 7);
                setPiece(move.fromRow(), 5, rook);
                setPiece(move.fromRow(), 7, null);
            } else {
                // Queenside: rook from a to d
                ChessPiece rook = getPiece(move.fromRow(), 0);
                setPiece(move.fromRow(), 3, rook);
                setPiece(move.fromRow(), 0, null);
            }
        }

        return captured;
    }

    /**
     * Creates a copy of this board.
     * 
     * @return board copy
     */
    public ChessBoard copy() {
        ChessBoard copy = new ChessBoard();
        for (int row = 0; row < 8; row++) {
            System.arraycopy(board[row], 0, copy.board[row], 0, 8);
        }
        return copy;
    }

    /**
     * Gets material value for a color.
     * 
     * @param color the color
     * @return total material value
     */
    public int getMaterialValue(ChessPiece.Color color) {
        int total = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && piece.getColor() == color) {
                    total += piece.getValue();
                }
            }
        }
        return total;
    }

    @Override
    public int getType() {
        return BoardInterface.SQUARE;
    }

    @Override
    public Graph<? extends TileInterface> getAllTiles() {
        return null;
    }

    @Override
    public java.util.List<PieceInterface> getPieces() {
        java.util.List<PieceInterface> pieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] != null) {
                    pieces.add(board[row][col]);
                }
            }
        }
        return pieces;
    }
}
