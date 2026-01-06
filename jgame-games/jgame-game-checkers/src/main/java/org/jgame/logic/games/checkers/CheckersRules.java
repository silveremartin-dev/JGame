package org.jgame.logic.games.checkers;

import org.jgame.logic.games.AbstractBoardGame;
import org.jgame.model.GameUser;
import org.jgame.parts.PlayerInterface;
import org.jgame.parts.BoardInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of American Checkers (Draughts) rules.
 */
public class CheckersRules extends AbstractBoardGame {

    // Board representation using CheckersBoard
    private final CheckersBoard board;

    // Piece tracking
    private List<CheckersPiece> player1Pieces;
    private List<CheckersPiece> player2Pieces;

    // Game state
    private int currentPlayer; // 1 or 2
    private boolean gameOver;
    private int winnerId; // 0 = none, 1 = player1, 2 = player2
    private CheckersMove lastMove;
    private boolean multiJumpInProgress; // For continuing multi-jump chains

    public CheckersRules() {
        super("Checkers", "1.0", "American Checkers");
        this.board = new CheckersBoard();
        initGame();
    }

    /**
     * Initializes a new game of checkers with standard setup.
     */
    public void initGame() {
        // Board is already created in constructor, but we fill it
        player1Pieces = new ArrayList<>();
        player2Pieces = new ArrayList<>();
        currentPlayer = 1;
        gameOver = false;
        winnerId = 0;
        multiJumpInProgress = false;

        // Place player 1 pieces (red/white) on rows 0-2
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                if (isDarkSquare(row, col)) {
                    CheckersPiece piece = new CheckersPiece(1, row, col);
                    board.setPiece(row, col, piece);
                    player1Pieces.add(piece);
                } else {
                    board.setPiece(row, col, null);
                }
            }
        }

        // Empty rows 3-4
        for (int row = 3; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                board.setPiece(row, col, null);
            }
        }

        // Place player 2 pieces (black) on rows 5-7
        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (isDarkSquare(row, col)) {
                    CheckersPiece piece = new CheckersPiece(2, row, col);
                    board.setPiece(row, col, piece);
                    player2Pieces.add(piece);
                } else {
                    board.setPiece(row, col, null);
                }
            }
        }
    }

    @Override
    public void addPlayer(GameUser player) {
        if (getPlayers().size() < 2) {
            super.addPlayer(player);
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

    @Override
    public List<PlayerInterface> getPlayers() {
        return super.getPlayers();
    }

    @Override
    public BoardInterface getBoard() {
        return board;
    }

    public CheckersBoard getCheckersBoard() {
        return board;
    }

    @Override
    public boolean isFinished() {
        return gameOver;
    }

    @Override
    public PlayerInterface getWinner() {
        if (winnerId > 0 && winnerId <= getPlayers().size()) {
            return getPlayers().get(winnerId - 1);
        }
        return null;
    }

    public int getWinnerId() {
        return winnerId;
    }

    /**
     * Checks if a square is a dark square (playable in checkers).
     * Dark squares have an odd sum of row + column.
     */
    private boolean isDarkSquare(int row, int col) {
        return (row + col) % 2 == 1;
    }

    /**
     * Checks if coordinates are within board bounds.
     */
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    /**
     * Validates if a move is legal according to checkers rules.
     */
    public boolean isValidMove(CheckersMove move) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        // Check bounds
        if (!inBounds(fromRow, fromCol) || !inBounds(toRow, toCol)) {
            return false;
        }

        // Check source has a piece belonging to current player
        CheckersPiece piece = board.getPiece(fromRow, fromCol);
        if (piece == null || piece.getPlayer() != currentPlayer) {
            return false;
        }

        // Check destination is empty
        if (board.getPiece(toRow, toCol) != null) {
            return false;
        }

        // Check diagonal movement
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        if (rowDiff != colDiff) {
            return false; // Must be diagonal
        }

        // Check move distance (1 for normal, 2 for capture)
        if (rowDiff != 1 && rowDiff != 2) {
            return false;
        }

        // Check direction for regular pieces (not kings)
        if (!piece.isKing()) {
            int forwardDir = (piece.getPlayer() == 1) ? 1 : -1;
            if ((toRow - fromRow) * forwardDir < 0) {
                return false; // Regular pieces can only move forward
            }
        }

        // If capture (distance 2), validate the jump
        if (rowDiff == 2) {
            int midRow = (fromRow + toRow) / 2;
            int midCol = (fromCol + toCol) / 2;
            CheckersPiece captured = board.getPiece(midRow, midCol);

            // Must have opponent piece to capture
            if (captured == null || captured.getPlayer() == piece.getPlayer()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets all possible jump moves for a specific piece.
     */
    private List<CheckersMove> getJumpsForPiece(CheckersPiece piece) {
        List<CheckersMove> jumps = new ArrayList<>();
        int row = piece.getRow();
        int col = piece.getCol();

        // Determine possible jump directions based on piece type
        int[][] directions;
        if (piece.isKing()) {
            // Kings can jump in all 4 diagonal directions
            directions = new int[][] { { -2, -2 }, { -2, 2 }, { 2, -2 }, { 2, 2 } };
        } else {
            // Regular pieces jump forward only
            int forwardDir = (piece.getPlayer() == 1) ? 1 : -1;
            directions = new int[][] { { 2 * forwardDir, -2 }, { 2 * forwardDir, 2 } };
        }

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            CheckersMove move = new CheckersMove(row, col, newRow, newCol);
            if (isValidMove(move)) {
                jumps.add(move);
            }
        }

        return jumps;
    }

    /**
     * Gets all mandatory jump moves for the current player.
     * Jumps are mandatory in checkers when available.
     */
    public List<CheckersMove> getMandatoryJumps() {
        return getMandatoryJumps(currentPlayer);
    }

    /**
     * Gets all mandatory jumps for a specific player.
     */
    private List<CheckersMove> getMandatoryJumps(int player) {
        List<CheckersMove> jumps = new ArrayList<>();
        List<CheckersPiece> pieces = (player == 1) ? player1Pieces : player2Pieces;

        for (CheckersPiece piece : pieces) {
            jumps.addAll(getJumpsForPiece(piece));
        }

        return jumps;
    }

    /**
     * Gets all possible non-jump moves for a piece.
     */
    private List<CheckersMove> getRegularMovesForPiece(CheckersPiece piece) {
        List<CheckersMove> moves = new ArrayList<>();
        int row = piece.getRow();
        int col = piece.getCol();

        // Determine possible move directions
        int[][] directions;
        if (piece.isKing()) {
            // Kings can move in all 4 diagonal directions
            directions = new int[][] { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
        } else {
            // Regular pieces move forward only
            int forwardDir = (piece.getPlayer() == 1) ? 1 : -1;
            directions = new int[][] { { forwardDir, -1 }, { forwardDir, 1 } };
        }

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            CheckersMove move = new CheckersMove(row, col, newRow, newCol);
            if (isValidMove(move)) {
                moves.add(move);
            }
        }

        return moves;
    }

    /**
     * Gets all legal moves for the current player.
     * If jumps are available, only jumps are returned (mandatory).
     */
    public List<CheckersMove> getAllLegalMoves() {
        // First check for mandatory jumps
        List<CheckersMove> jumps = getMandatoryJumps();
        if (!jumps.isEmpty()) {
            return jumps; // Must jump if possible
        }

        // Otherwise, return all regular moves
        List<CheckersMove> moves = new ArrayList<>();
        List<CheckersPiece> pieces = (currentPlayer == 1) ? player1Pieces : player2Pieces;

        for (CheckersPiece piece : pieces) {
            moves.addAll(getRegularMovesForPiece(piece));
        }

        return moves;
    }

    /**
     * Executes a move, handling captures, multi-jumps, and king promotion.
     * Returns true if the move completed the turn, false if multi-jump continues.
     */
    public boolean makeMove(CheckersMove move) {
        if (!isValidMove(move)) {
            throw new IllegalArgumentException("Invalid move: " + move);
        }

        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        CheckersPiece piece = board.getPiece(fromRow, fromCol);

        // Move the piece
        board.setPiece(toRow, toCol, piece);
        board.setPiece(fromRow, fromCol, null);
        piece.setPosition(toRow, toCol);

        boolean turnComplete = true;

        // Handle capture
        if (move.isCapture()) {
            int midRow = (fromRow + toRow) / 2;
            int midCol = (fromCol + toCol) / 2;
            CheckersPiece captured = board.getPiece(midRow, midCol);

            // Remove captured piece
            board.setPiece(midRow, midCol, null);
            if (captured.getPlayer() == 1) {
                player1Pieces.remove(captured);
            } else {
                player2Pieces.remove(captured);
            }

            // Check for additional jumps (multi-jump)
            List<CheckersMove> nextJumps = getJumpsForPiece(piece);
            if (!nextJumps.isEmpty()) {
                multiJumpInProgress = true;
                turnComplete = false; // Turn continues with multi-jump
            } else {
                multiJumpInProgress = false;
            }
        }

        // Check for king promotion (only at end of turn)
        if (turnComplete && !piece.isKing()) {
            int promotionRow = (piece.getPlayer() == 1) ? 7 : 0;
            if (piece.getRow() == promotionRow) {
                piece.promoteToKing();
            }
        }

        lastMove = move;

        // Complete turn if no multi-jump
        if (turnComplete) {
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
            checkWinConditions();
        }

        return turnComplete;
    }

    /**
     * Checks if the game has been won.
     */
    private void checkWinConditions() {
        List<CheckersPiece> opponentPieces = (currentPlayer == 1) ? player1Pieces : player2Pieces;

        // Win if opponent has no pieces
        if (opponentPieces.isEmpty()) {
            gameOver = true;
            winnerId = (currentPlayer == 1) ? 2 : 1; // Previous player wins
            return;
        }

        // Win if opponent has no legal moves
        if (getAllLegalMoves().isEmpty()) {
            gameOver = true;
            winnerId = (currentPlayer == 1) ? 2 : 1; // Previous player wins
        }
    }

    // Getters for game state

    public CheckersPiece getPiece(int row, int col) {
        if (!inBounds(row, col))
            return null;
        return board.getPiece(row, col);
    }

    public CheckersPiece getPieceAt(int row, int col) {
        return getPiece(row, col);
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getPieceCount(int player) {
        return (player == 1) ? player1Pieces.size() : player2Pieces.size();
    }

    public boolean isMultiJumpInProgress() {
        return multiJumpInProgress;
    }

    public CheckersMove getLastMove() {
        return lastMove;
    }

    /**
     * Returns a string representation of the board for debugging.
     */
    public String getBoardString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  0 1 2 3 4 5 6 7\n");
        for (int row = 0; row < 8; row++) {
            sb.append(row).append(" ");
            for (int col = 0; col < 8; col++) {
                CheckersPiece piece = board.getPiece(row, col);
                if (piece == null) {
                    sb.append(isDarkSquare(row, col) ? "· " : "  ");
                } else {
                    char symbol = piece.isKing() ? (piece.getPlayer() == 1 ? 'W' : 'B')
                            : (piece.getPlayer() == 1 ? 'w' : 'b');
                    sb.append(symbol).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getGameName() {
        return "Checkers";
    }

    public int getMinPlayers() {
        return 2;
    }

    public int getMaxPlayers() {
        return 2;
    }
}
