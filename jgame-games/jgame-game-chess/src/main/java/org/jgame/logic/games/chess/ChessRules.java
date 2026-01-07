package org.jgame.logic.games.chess;

import org.jgame.logic.ActionInterface;
import org.jgame.logic.engine.GameAction;
import org.jgame.logic.engine.GameState;
import org.jgame.logic.games.AbstractBoardGame;
import org.jgame.model.GameUser;
import org.jgame.parts.PlayerInterface;
import org.jgame.parts.BoardInterface;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Chess game rules implementation.
 */
public class ChessRules extends AbstractBoardGame {

    private final ChessBoard board;
    private ChessPiece.Color currentTurn;
    private boolean gameFinished;
    private PlayerInterface winner;

    // State for special moves
    private boolean whiteKingMoved = false;
    private boolean blackKingMoved = false;
    private boolean[] whiteRooksMoved = { false, false }; // [0] = a-file (Queenside), [1] = h-file (Kingside)
    private boolean[] blackRooksMoved = { false, false };
    private ChessMove lastMove = null;

    public ChessRules() {
        super("Chess", "1.0", "Standard Chess Game");
        this.board = new ChessBoard();
        this.currentTurn = ChessPiece.Color.WHITE;
        this.gameFinished = false;
    }

    public void resign() {
        if (!gameFinished) {
            gameFinished = true;
            // Current turn player resigns, so the OTHER player wins
            winner = (currentTurn == ChessPiece.Color.WHITE)
                    ? (getPlayers().size() > 1 ? getPlayers().get(1) : null)
                    : (getPlayers().size() > 0 ? getPlayers().get(0) : null);
        }
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

    public ChessBoard getChessBoard() {
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

    public void setCurrentTurn(ChessPiece.Color color) {
        this.currentTurn = color;
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

        // Reconstruct move with special flags if necessary
        ChessMove moveToExecute = move;
        if (piece instanceof King && Math.abs(move.toCol() - move.fromCol()) == 2) {
            moveToExecute = ChessMove.castling(move.fromRow(), move.fromCol(), move.toRow(), move.toCol());
        } else if (piece instanceof Pawn) {
            if (move.toCol() != move.fromCol() && board.isEmpty(move.toRow(), move.toCol())) {
                moveToExecute = ChessMove.enPassant(move.fromRow(), move.fromCol(), move.toRow(), move.toCol());
            } else {
                int promotionRow = (piece.getColor() == ChessPiece.Color.WHITE) ? 0 : 7;
                if (move.toRow() == promotionRow && move.promotionPiece() == null) {
                    // Default to Queen if no piece specified
                    moveToExecute = ChessMove.promotion(move.fromRow(), move.fromCol(), move.toRow(), move.toCol(),
                            Queen.class);
                }
            }
        }

        updateMovementState(moveToExecute, piece);
        board.makeMove(moveToExecute);
        lastMove = moveToExecute;
        currentTurn = currentTurn.opposite();

        checkGameEnd();

        return true;
    }

    private void updateMovementState(ChessMove move, ChessPiece piece) {
        if (piece instanceof King) {
            if (piece.getColor() == ChessPiece.Color.WHITE)
                whiteKingMoved = true;
            else
                blackKingMoved = true;
        } else if (piece instanceof Rook) {
            if (piece.getColor() == ChessPiece.Color.WHITE) {
                if (move.fromRow() == 7 && move.fromCol() == 0)
                    whiteRooksMoved[0] = true;
                else if (move.fromRow() == 7 && move.fromCol() == 7)
                    whiteRooksMoved[1] = true;
            } else {
                if (move.fromRow() == 0 && move.fromCol() == 0)
                    blackRooksMoved[0] = true;
                else if (move.fromRow() == 0 && move.fromCol() == 7)
                    blackRooksMoved[1] = true;
            }
        }
    }

    public boolean isValidMove(ChessMove move) {
        ChessPiece piece = board.getPiece(move.fromRow(), move.fromCol());
        if (piece == null)
            return false;

        boolean baseMoveValid = switch (piece) {
            case Pawn p -> isValidPawnMove(move, p);
            case Knight _ -> isValidKnightMove(move);
            case Bishop _ -> isValidBishopMove(move);
            case Rook _ -> isValidRookMove(move);
            case Queen _ -> isValidQueenMove(move);
            case King _ -> isValidKingMove(move);
        };

        if (!baseMoveValid) {
            return false;
        }

        // Check if move leaves current player's king in check
        ChessBoard tempBoard = board.copy();
        tempBoard.makeMove(move);
        return !isInCheck(tempBoard, piece.getColor());
    }

    public boolean isInCheck(ChessPiece.Color color) {
        return isInCheck(this.board, color);
    }

    private boolean isInCheck(ChessBoard board, ChessPiece.Color color) {
        // Find king
        int kingRow = -1;
        int kingCol = -1;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece p = board.getPiece(r, c);
                if (p instanceof King && p.getColor() == color) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
        }

        if (kingRow == -1)
            return false; // Should not happen in normal game

        return isSquareAttacked(board, kingRow, kingCol, color.opposite());
    }

    private boolean isSquareAttacked(ChessBoard board, int row, int col, ChessPiece.Color attackerColor) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece p = board.getPiece(r, c);
                if (p != null && p.getColor() == attackerColor) {
                    if (p instanceof Pawn) {
                        if (isPawnAttacking(board, r, c, row, col, attackerColor)) {
                            return true;
                        }
                    } else {
                        ChessMove attackMove = new ChessMove(r, c, row, col);
                        if (isValidBaseMove(board, attackMove, p)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isValidBaseMove(ChessBoard board, ChessMove move, ChessPiece piece) {
        return switch (piece) {
            case Pawn p -> isValidPawnMove(board, move, p);
            case Knight _ -> isValidKnightMove(board, move, piece.getColor());
            case Bishop _ -> isValidBishopMove(board, move, piece.getColor());
            case Rook _ -> isValidRookMove(board, move, piece.getColor());
            case Queen _ -> isValidQueenMove(board, move, piece.getColor());
            case King _ -> isValidKingMove(board, move, piece.getColor());
        };
    }

    private boolean isValidPawnMove(ChessMove move, Pawn pawn) {
        return isValidPawnMove(this.board, move, pawn);
    }

    private boolean isValidPawnMove(ChessBoard board, ChessMove move, Pawn pawn) {
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
            if (target != null && target.getColor() != pawn.getColor()) {
                return true;
            }
            // En Passant
            if (target == null && lastMove != null && lastMove.toRow() == move.fromRow()
                    && lastMove.toCol() == move.toCol()) {
                ChessPiece lastPiece = board.getPiece(lastMove.toRow(), lastMove.toCol());
                if (lastPiece instanceof Pawn && Math.abs(lastMove.toRow() - lastMove.fromRow()) == 2) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isPawnAttacking(ChessBoard board, int fromRow, int fromCol, int toRow, int toCol,
            ChessPiece.Color pawnColor) {
        int direction = pawnColor == ChessPiece.Color.WHITE ? -1 : 1;
        int rowDiff = toRow - fromRow;
        int colDiff = Math.abs(toCol - fromCol);
        return colDiff == 1 && rowDiff == direction;
    }

    private boolean isValidKnightMove(ChessMove move) {
        return isValidKnightMove(this.board, move, currentTurn);
    }

    private boolean isValidKnightMove(ChessBoard board, ChessMove move, ChessPiece.Color color) {
        int rowDiff = Math.abs(move.toRow() - move.fromRow());
        int colDiff = Math.abs(move.toCol() - move.fromCol());

        boolean validPattern = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
        if (!validPattern)
            return false;

        return !isOwnPiece(board, move.toRow(), move.toCol(), color);
    }

    private boolean isValidBishopMove(ChessMove move) {
        return isValidBishopMove(this.board, move, currentTurn);
    }

    private boolean isValidBishopMove(ChessBoard board, ChessMove move, ChessPiece.Color color) {
        int rowDiff = Math.abs(move.toRow() - move.fromRow());
        int colDiff = Math.abs(move.toCol() - move.fromCol());

        if (rowDiff != colDiff)
            return false;

        return isPathClear(board, move) && !isOwnPiece(board, move.toRow(), move.toCol(), color);
    }

    private boolean isValidRookMove(ChessMove move) {
        return isValidRookMove(this.board, move, currentTurn);
    }

    private boolean isValidRookMove(ChessBoard board, ChessMove move, ChessPiece.Color color) {
        boolean straightLine = move.fromRow() == move.toRow() || move.fromCol() == move.toCol();
        if (!straightLine)
            return false;

        return isPathClear(board, move) && !isOwnPiece(board, move.toRow(), move.toCol(), color);
    }

    private boolean isValidQueenMove(ChessMove move) {
        return isValidQueenMove(this.board, move, currentTurn);
    }

    private boolean isValidQueenMove(ChessBoard board, ChessMove move, ChessPiece.Color color) {
        return isValidBishopMove(board, move, color) || isValidRookMove(board, move, color);
    }

    private boolean isValidKingMove(ChessMove move) {
        return isValidKingMove(this.board, move, currentTurn);
    }

    private boolean isValidKingMove(ChessBoard board, ChessMove move, ChessPiece.Color color) {
        int rowDiff = Math.abs(move.toRow() - move.fromRow());
        int colDiff = Math.abs(move.toCol() - move.fromCol());

        if (rowDiff <= 1 && colDiff <= 1) {
            return !isOwnPiece(board, move.toRow(), move.toCol(), color);
        }

        // Castling
        if (rowDiff == 0 && colDiff == 2) {
            return isValidCastlingMove(board, move, color);
        }

        return false;
    }

    private boolean isValidCastlingMove(ChessBoard board, ChessMove move, ChessPiece.Color color) {
        if (isInCheck(board, color))
            return false;

        boolean moved = (color == ChessPiece.Color.WHITE) ? whiteKingMoved : blackKingMoved;
        if (moved)
            return false;

        int row = (color == ChessPiece.Color.WHITE) ? 7 : 0;
        if (move.toRow() != row)
            return false;

        boolean kingside = move.toCol() > move.fromCol();
        int rookCol = kingside ? 7 : 0;
        boolean rookMoved = (color == ChessPiece.Color.WHITE) ? whiteRooksMoved[kingside ? 1 : 0]
                : blackRooksMoved[kingside ? 1 : 0];

        if (rookMoved)
            return false;

        // Check path is clear
        int direction = kingside ? 1 : -1;
        for (int c = move.fromCol() + direction; c != rookCol; c += direction) {
            if (!board.isEmpty(row, c))
                return false;
        }

        // Check if king passes through check
        if (isSquareAttacked(board, row, move.fromCol() + direction, color.opposite()))
            return false;

        return true;
    }

    private boolean isPathClear(ChessBoard board, ChessMove move) {
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

    private boolean isOwnPiece(ChessBoard board, int row, int col, ChessPiece.Color color) {
        ChessPiece piece = board.getPiece(row, col);
        return piece != null && piece.getColor() == color;
    }

    private void checkGameEnd() {
        if (gameFinished)
            return;

        // Check for checkmate or stalemate
        boolean hasLegalMoves = false;
        outer: for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece piece = board.getPiece(r, c);
                if (piece != null && piece.getColor() == currentTurn) {
                    for (int tr = 0; tr < 8; tr++) {
                        for (int tc = 0; tc < 8; tc++) {
                            if (isValidMove(new ChessMove(r, c, tr, tc))) {
                                hasLegalMoves = true;
                                break outer;
                            }
                        }
                    }
                }
            }
        }

        if (!hasLegalMoves) {
            gameFinished = true;
            if (isInCheck(currentTurn)) {
                // Checkmate
                winner = (currentTurn == ChessPiece.Color.WHITE)
                        ? (getPlayers().size() > 1 ? getPlayers().get(1) : null)
                        : (getPlayers().size() > 0 ? getPlayers().get(0) : null);
            } else {
                // Stalemate
                winner = null;
            }
        }
    }

    @Override
    public GameState toGameState() {
        List<String> playerIds = getPlayers().stream()
                .map(PlayerInterface::getId)
                .collect(Collectors.toList());

        Map<String, Object> boardState = new HashMap<>();
        // Serialize board: simple map of positions to piece types
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece p = board.getPiece(r, c);
                if (p != null) {
                    boardState.put(r + "," + c, p.getClass().getSimpleName() + ":" + p.getColor());
                }
            }
        }

        return new GameState(
                "chess",
                UUID.randomUUID().toString(),
                playerIds,
                currentTurn == ChessPiece.Color.WHITE ? 0 : 1,
                0, // Turn number not strictly tracked yet
                gameFinished ? GameState.GamePhase.FINISHED : GameState.GamePhase.PLAYING,
                boardState,
                Map.of(),
                getLegalActions(getPlayers().get(currentTurn == ChessPiece.Color.WHITE ? 0 : 1)).stream()
                        .map(a -> (GameAction) a)
                        .collect(Collectors.toList()),
                List.of(),
                Instant.now(),
                Instant.now());
    }

    @Override
    public Set<ActionInterface> getLegalActions(PlayerInterface player) {
        Set<ActionInterface> legalActions = new HashSet<>();
        if (gameFinished)
            return legalActions;

        // Ensure it's the right player's turn
        int playerIndex = getPlayers().indexOf(player);
        ChessPiece.Color playerColor = (playerIndex == 0) ? ChessPiece.Color.WHITE : ChessPiece.Color.BLACK;
        if (playerColor != currentTurn)
            return legalActions;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece piece = board.getPiece(r, c);
                if (piece != null && piece.getColor() == currentTurn) {
                    // Try all possible destination squares
                    for (int tr = 0; tr < 8; tr++) {
                        for (int tc = 0; tc < 8; tc++) {
                            ChessMove move = new ChessMove(r, c, tr, tc);
                            if (isValidMove(move)) {
                                // Map ChessMove to GameAction
                                Map<String, Object> params = Map.of(
                                        "fromRow", r,
                                        "fromCol", c,
                                        "toRow", tr,
                                        "toCol", tc);
                                legalActions.add(GameAction.create(player.getId(), GameAction.TYPE_MOVE, params));
                            }
                        }
                    }
                }
            }
        }
        return legalActions;
    }

    @Override
    public void executeAction(PlayerInterface player, ActionInterface action) {
        if (action instanceof GameAction ga && GameAction.TYPE_MOVE.equals(ga.actionType())) {
            int fromRow = (int) ga.parameters().get("fromRow");
            int fromCol = (int) ga.parameters().get("fromCol");
            int toRow = (int) ga.parameters().get("toRow");
            int toCol = (int) ga.parameters().get("toCol");

            ChessMove move = new ChessMove(fromRow, fromCol, toRow, toCol);
            makeMove(move);
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
