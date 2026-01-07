package org.jgame.logic.games.chess;

import org.jgame.ai.MinimaxAI;
import org.jgame.logic.engine.GameAction;
import org.jgame.logic.engine.GameState;

import java.util.*;

/**
 * Chess-specific Minimax AI implementation.
 */
public class ChessMinimaxAI extends MinimaxAI {

    public ChessMinimaxAI(int depth) {
        super("Chess Minimax", depth);
    }

    @Override
    protected int evaluate(GameState state) {
        if (state.phase() == GameState.GamePhase.FINISHED) {
            if (state.currentPlayerIndex() == 0) {
                // It was white's turn, but no moves left -> White lost (if in check) or
                // stalemate
                // Actually evaluate should be from state perspective
                return -100000; // Large penalty for losing
            } else {
                return 100000;
            }
        }

        // Simple evaluation based on material balance
        int whiteValue = 0;
        int blackValue = 0;

        for (Map.Entry<String, Object> entry : state.boardState().entrySet()) {
            String val = (String) entry.getValue();
            if (val.contains("WHITE")) {
                whiteValue += getPieceValue(val.split(":")[0]);
            } else if (val.contains("BLACK")) {
                blackValue += getPieceValue(val.split(":")[0]);
            }
        }

        return whiteValue - blackValue;
    }

    private int getPieceValue(String type) {
        return switch (type) {
            case "Pawn" -> 100;
            case "Knight" -> 320;
            case "Bishop" -> 330;
            case "Rook" -> 500;
            case "Queen" -> 900;
            case "King" -> 20000;
            default -> 0;
        };
    }

    @Override
    protected GameState applyMove(GameState state, GameAction action) {
        // Use ChessRules to apply the move and get the next state
        ChessRules rules = new ChessRules();

        // Reconstruct board from state
        ChessBoard board = rules.getChessBoard();
        // Clear board first? ChessRules() initializes starting position.
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                board.setPiece(r, c, null);

        for (Map.Entry<String, Object> entry : state.boardState().entrySet()) {
            String[] pos = entry.getKey().split(",");
            int r = Integer.parseInt(pos[0]);
            int c = Integer.parseInt(pos[1]);
            String val = (String) entry.getValue();
            String[] parts = val.split(":");
            ChessPiece.Color color = ChessPiece.Color.valueOf(parts[1]);
            ChessPiece piece = switch (parts[0]) {
                case "Pawn" -> new Pawn(color);
                case "Knight" -> new Knight(color);
                case "Bishop" -> new Bishop(color);
                case "Rook" -> new Rook(color);
                case "Queen" -> new Queen(color);
                case "King" -> new King(color);
                default -> null;
            };
            board.setPiece(r, c, piece);
        }

        // Set turn
        rules.setCurrentTurn(state.currentPlayerIndex() == 0 ? ChessPiece.Color.WHITE : ChessPiece.Color.BLACK);

        int fromRow = (int) action.parameters().get("fromRow");
        int fromCol = (int) action.parameters().get("fromCol");
        int toRow = (int) action.parameters().get("toRow");
        int toCol = (int) action.parameters().get("toCol");

        rules.makeMove(new ChessMove(fromRow, fromCol, toRow, toCol));

        return rules.toGameState();
    }
}
