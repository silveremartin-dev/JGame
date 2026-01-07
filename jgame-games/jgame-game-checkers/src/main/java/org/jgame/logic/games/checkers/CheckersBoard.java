package org.jgame.logic.games.checkers;

import org.jgame.parts.BoardInterface;
import org.jgame.parts.PieceInterface;
import org.jgame.parts.TileInterface;
import org.jgame.parts.boards.AbstractBoard;
import org.jgame.util.Graph;

import java.util.ArrayList;
import java.util.List;

public class CheckersBoard extends AbstractBoard {

    // 8x8 grid
    private final CheckersPiece[][] grid;

    public CheckersBoard() {
        this.grid = new CheckersPiece[8][8];
    }

    public CheckersPiece[][] getGrid() {
        return grid;
    }

    public CheckersPiece getPiece(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8)
            return null;
        return grid[row][col];
    }

    public void setPiece(int row, int col, CheckersPiece piece) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            grid[row][col] = piece;
            if (piece != null) {
                piece.setPosition(row, col);
            }
        }
    }

    public boolean isEmpty(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8)
            return false;
        return grid[row][col] == null;
    }

    @Override
    public int getType() {
        return BoardInterface.SQUARE; // Approximation
    }

    @Override
    public Graph<? extends TileInterface> getAllTiles() {
        // Not implemented (Legacy Adapter)
        return null;
    }

    @Override
    public List<PieceInterface> getPieces() {
        List<PieceInterface> pieces = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (grid[r][c] != null) {
                    pieces.add(grid[r][c]);
                }
            }
        }
        return pieces;
    }

    public CheckersBoard copy() {
        CheckersBoard newBoard = new CheckersBoard();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (grid[r][c] != null) {
                    newBoard.setPiece(r, c, grid[r][c].copy());
                }
            }
        }
        return newBoard;
    }
}
