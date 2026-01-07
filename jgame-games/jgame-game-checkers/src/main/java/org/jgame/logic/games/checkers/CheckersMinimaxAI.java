package org.jgame.logic.games.checkers;

import org.jgame.ai.MinimaxAI;
import org.jgame.logic.engine.GameAction;
import org.jgame.logic.engine.GameState;

import java.util.Map;

/**
 * Checkers-specific Minimax AI implementation.
 */
public class CheckersMinimaxAI extends MinimaxAI {

    public CheckersMinimaxAI(int depth) {
        super("Checkers Minimax", depth);
    }

    @Override
    protected int evaluate(GameState state) {
        int player1Value = 0;
        int player2Value = 0;

        for (Map.Entry<String, Object> entry : state.boardState().entrySet()) {
            String val = (String) entry.getValue();
            String[] parts = val.split(":");
            int player = Integer.parseInt(parts[0]);
            boolean isKing = Boolean.parseBoolean(parts[1]);

            int pieceValue = isKing ? 300 : 100;

            if (player == 1) {
                player1Value += pieceValue;
            } else {
                player2Value += pieceValue;
            }
        }

        return player1Value - player2Value;
    }

    @Override
    protected GameState applyMove(GameState state, GameAction action) {
        CheckersRules rules = new CheckersRules();

        // Reconstruct board from state
        CheckersBoard board = rules.getCheckersBoard();
        // Clear board first? CheckersRules() initializes starting position.
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                board.setPiece(r, c, null);

        for (Map.Entry<String, Object> entry : state.boardState().entrySet()) {
            String[] pos = entry.getKey().split(",");
            int r = Integer.parseInt(pos[0]);
            int c = Integer.parseInt(pos[1]);
            String val = (String) entry.getValue();
            String[] parts = val.split(":");
            int player = Integer.parseInt(parts[0]);
            boolean isKing = Boolean.parseBoolean(parts[1]);

            CheckersPiece piece = new CheckersPiece(player, r, c);
            if (isKing)
                piece.promoteToKing();
            board.setPiece(r, c, piece);
        }

        // Set current player in rules
        rules.setCurrentPlayer(state.currentPlayerIndex() + 1);

        int fromRow = (int) action.parameters().get("fromRow");
        int fromCol = (int) action.parameters().get("fromCol");
        int toRow = (int) action.parameters().get("toRow");
        int toCol = (int) action.parameters().get("toCol");

        rules.makeMove(new CheckersMove(fromRow, fromCol, toRow, toCol));

        return rules.toGameState();
    }
}
