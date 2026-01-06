package org.jgame.logic.games.chess;

import org.jgame.logic.games.AbstractBoardGame;
import org.jgame.model.GameUser;
import org.jgame.parts.PlayerInterface;
import org.jgame.parts.BoardInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Chess game rules implementation.
 */
public class ChessRules extends AbstractBoardGame {

    private final ChessBoard board;
    // players in AbstractGame
    private ChessPiece.Color currentTurn;
    private boolean gameFinished;
    private PlayerInterface winner;

    public ChessRules() {
        super("Chess", "1.0", "Standard Chess Game");
        this.board = new ChessBoard();
        this.currentTurn = ChessPiece.Color.WHITE;
        this.gameFinished = false;
    }

    @Override
    public void addPlayer(GameUser player) {
        if (getPlayers().size() < 2) {
            super.addPlayer(player);
            // Player index 0 = White, index 1 = Black (implicit by turn order)
        }
    }

    public List<GameUser> getGameUsers() {
        List<GameUser> users = new ArrayList<>();
        for (PlayerInterface p : super.getPlayers()) {
            if (p instanceof org.jgame.parts.players.GamePlayer) {
                users.add(((org.jgame.parts.players.GamePlayer) p).getUser());
            }
        }
        return users;
    }

    // AbstractMethod from AbstractBoardGame
    @Override
    public BoardInterface getBoard() {
        return board;
    }

    @Override
    public boolean isFinished() {
        return gameFinished;
    }

    @Override
    public PlayerInterface getWinner() {
        return winner;
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

    public boolean isValidMove(ChessMove move) {
        ChessPiece piece = board.getPiece(move.fromRow(), move.fromCol());
        if (piece == null)
            return false;

        return switch (piece) {
            case Pawn p -> isValidPawnMove(move, p);
            case Knight _ -> isValidKnightMove(move);
            case Bishop _ -> isValidBishopMove(move);
            case Rook _ -> isValidRookMove(move);
            case Queen _ -> isValidQueenMove(move);
            case King _ -> isValidKingMove(move);
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
            winner = super.getPlayers().size() > 1 ? super.getPlayers().get(1) : null;
        } else if (!blackKingExists) {
            gameFinished = true;
            winner = super.getPlayers().size() > 0 ? super.getPlayers().get(0) : null;
        }
    }

    public String getGameName() {
        return "Chess";
    }

    public int getMinPlayers() {
        return 2;
    }

    public int getMaxPlayers() {
        return 2;
    }
}
