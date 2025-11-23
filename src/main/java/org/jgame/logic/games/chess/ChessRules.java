/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.logic.games.chess;

import org.jgame.logic.engine.GameRules;
import org.jgame.server.GameUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Chess game rules implementation.
 */
public class ChessRules extends GameRules {

    private final ChessBoard board;
    private final List<GameUser> players;
    private ChessPiece.Color currentTurn;
    private boolean gameFinished;
    private GameUser winner;

    public ChessRules() {
        this.board = new ChessBoard();
        this.players = new ArrayList<>();
        this.currentTurn = ChessPiece.Color.WHITE;
        this.gameFinished = false;
    }

    public void addPlayer(GameUser player) {
        if (players.size() < 2) {
            players.add(player);
        }
    }

    @Override
    public List<GameUser> getPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public boolean isFinished() {
        return gameFinished;
    }

    @Override
    public GameUser getWinner() {
        return winner;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public ChessPiece.Color getCurrentTurn() {
        return currentTurn;
    }

    public boolean makeMove(ChessMove move) {
        if (gameFinished)
            return false;

        ChessPiece piece = board.getPiece(move.fromRow(), move.fromCol());
        if (piece == null || piece.getColor() != currentTurn) {
            return false;
        }

        if (!isValidMove(move)) {
            return false;
        }

        board.makeMove(move);
        currentTurn = currentTurn.opposite();

        checkGameEnd();

        return true;
    }

    private boolean isValidMove(ChessMove move) {
        ChessPiece piece = board.getPiece(move.fromRow(), move.fromCol());
        if (piece == null)
            return false;

        return switch (piece) {
            case Pawn p -> isValidPawnMove(move, p);
            case Knight n -> isValidKnightMove(move);
            case Bishop b -> isValidBishopMove(move);
            case Rook r -> isValidRookMove(move);
            case Queen q -> isValidQueenMove(move);
            case King k -> isValidKingMove(move);
        };
    }

    private boolean isValidPawnMove(ChessMove move, Pawn pawn) {
        int direction = pawn.getColor() == ChessPiece.Color.WHITE ? -1 : 1;
        int rowDiff = move.toRow() - move.fromRow();
        int colDiff = Math.abs(move.toCol() - move.fromCol());

        if (colDiff == 0 && rowDiff == direction && board.isEmpty(move.toRow(), move.toCol())) {
            return true;
        }

        int startRow = pawn.getColor() == ChessPiece.Color.WHITE ? 6 : 1;
        if (colDiff == 0 && rowDiff == 2 * direction && move.fromRow() == startRow) {
            return board.isEmpty(move.toRow(), move.toCol()) &&
                    board.isEmpty(move.fromRow() + direction, move.fromCol());
        }

        if (colDiff == 1 && rowDiff == direction) {
            ChessPiece target = board.getPiece(move.toRow(), move.toCol());
            return target != null && target.getColor() != pawn.getColor();
        }

        return false;
    }

    private boolean isValidKnightMove(ChessMove move) {
        int rowDiff = Math.abs(move.toRow() - move.fromRow());
        int colDiff = Math.abs(move.toCol() - move.fromCol());

        boolean validPattern = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
        if (!validPattern)
            return false;

        return !isOwnPiece(move.toRow(), move.toCol(), currentTurn);
    }

    private boolean isValidBishopMove(ChessMove move) {
        int rowDiff = Math.abs(move.toRow() - move.fromRow());
        int colDiff = Math.abs(move.toCol() - move.fromCol());

        if (rowDiff != colDiff)
            return false;

        return isPathClear(move) && !isOwnPiece(move.toRow(), move.toCol(), currentTurn);
    }

    private boolean isValidRookMove(ChessMove move) {
        boolean straightLine = move.fromRow() == move.toRow() || move.fromCol() == move.toCol();
        if (!straightLine)
            return false;

        return isPathClear(move) && !isOwnPiece(move.toRow(), move.toCol(), currentTurn);
    }

    private boolean isValidQueenMove(ChessMove move) {
        return isValidBishopMove(move) || isValidRookMove(move);
    }

    private boolean isValidKingMove(ChessMove move) {
        int rowDiff = Math.abs(move.toRow() - move.fromRow());
        int colDiff = Math.abs(move.toCol() - move.fromCol());

        if (rowDiff > 1 || colDiff > 1)
            return false;

        return !isOwnPiece(move.toRow(), move.toCol(), currentTurn);
    }

    private boolean isPathClear(ChessMove move) {
        int rowStep = Integer.signum(move.toRow() - move.fromRow());
        int colStep = Integer.signum(move.toCol() - move.fromCol());

        int row = move.fromRow() + rowStep;
        int col = move.fromCol() + colStep;

        while (row != move.toRow() || col != move.toCol()) {
            if (!board.isEmpty(row, col)) {
                return false;
            }
            row += rowStep;
            col += colStep;
        }

        return true;
    }

    private boolean isOwnPiece(int row, int col, ChessPiece.Color color) {
        ChessPiece piece = board.getPiece(row, col);
        return piece != null && piece.getColor() == color;
    }

    private void checkGameEnd() {
        boolean whiteKingExists = false;
        boolean blackKingExists = false;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece p = board.getPiece(r, c);
                if (p instanceof King) {
                    if (p.getColor() == ChessPiece.Color.WHITE)
                        whiteKingExists = true;
                    if (p.getColor() == ChessPiece.Color.BLACK)
                        blackKingExists = true;
                }
            }
        }

        if (!whiteKingExists) {
            gameFinished = true;
            winner = players.size() > 1 ? players.get(1) : null;
        } else if (!blackKingExists) {
            gameFinished = true;
            winner = players.size() > 0 ? players.get(0) : null;
        }
    }
}
