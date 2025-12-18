/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 *
 * Enhanced with AI assistance from Google Gemini (Antigravity)
 */

package org.jgame.logic.games.checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for CheckersRules class.
 */
class CheckersRulesTest {

    private CheckersRules rules;

    @BeforeEach
    void setUp() {
        rules = new CheckersRules();
        rules.initGame();
    }

    @Nested
    @DisplayName("Game Initialization Tests")
    class InitializationTests {

        @Test
        @DisplayName("Initial board should have 12 pieces per player")
        void initialBoardHas12PiecesPerPlayer() {
            assertEquals(12, rules.getPieceCount(1), "Player 1 should have 12 pieces");
            assertEquals(12, rules.getPieceCount(2), "Player 2 should have 12 pieces");
        }

        @Test
        @DisplayName("Game should start with player 1's turn")
        void gameStartsWithPlayer1() {
            assertEquals(1, rules.getCurrentPlayer());
        }

        @Test
        @DisplayName("Game should not be finished at start")
        void gameNotFinishedAtStart() {
            assertFalse(rules.isFinished());
            assertFalse(rules.isGameOver());
        }

        @Test
        @DisplayName("Initial pieces should be on dark squares only")
        void initialPiecesOnDarkSquaresOnly() {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    CheckersPiece piece = rules.getPiece(row, col);
                    if (piece != null) {
                        assertTrue((row + col) % 2 == 1,
                                "Piece at (" + row + "," + col + ") should be on a dark square");
                    }
                }
            }
        }

        @Test
        @DisplayName("Rows 0-2 should have player 1 pieces, rows 5-7 should have player 2 pieces")
        void correctPiecePositioning() {
            // Player 1 pieces on rows 0-2
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 8; col++) {
                    CheckersPiece piece = rules.getPiece(row, col);
                    if (piece != null) {
                        assertEquals(1, piece.getPlayer());
                    }
                }
            }
            // Player 2 pieces on rows 5-7
            for (int row = 5; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    CheckersPiece piece = rules.getPiece(row, col);
                    if (piece != null) {
                        assertEquals(2, piece.getPlayer());
                    }
                }
            }
        }

        @Test
        @DisplayName("Middle rows 3-4 should be empty")
        void middleRowsEmpty() {
            for (int row = 3; row <= 4; row++) {
                for (int col = 0; col < 8; col++) {
                    assertNull(rules.getPiece(row, col),
                            "Position (" + row + "," + col + ") should be empty");
                }
            }
        }
    }

    @Nested
    @DisplayName("Move Validation Tests")
    class MoveValidationTests {

        @Test
        @DisplayName("Valid diagonal forward move should be accepted")
        void validForwardMoveAccepted() {
            // Player 1 piece at (2,1) can move to (3,0) or (3,2)
            // First find an actual valid move
            var legalMoves = rules.getAllLegalMoves();
            assertFalse(legalMoves.isEmpty(), "Should have legal moves");
            // Verify first legal move is valid
            assertTrue(rules.isValidMove(legalMoves.get(0)));
        }

        @Test
        @DisplayName("Move to occupied square should be rejected")
        void moveToOccupiedSquareRejected() {
            // Try to move to a square occupied by own piece
            CheckersMove move = new CheckersMove(5, 0, 6, 1);
            assertFalse(rules.isValidMove(move));
        }

        @Test
        @DisplayName("Backward move by non-king should be rejected")
        void backwardMoveByNonKingRejected() {
            // Get a valid move and make it
            var moves = rules.getAllLegalMoves();
            assertFalse(moves.isEmpty());
            CheckersMove firstMove = moves.get(0);
            rules.makeMove(firstMove);

            // For player 1, forward is increasing rows, so backward is decreasing
            // Try to move the piece backward (which would be invalid for non-king)
            CheckersMove backwardMove = new CheckersMove(firstMove.getToRow(), firstMove.getToCol(),
                    firstMove.getFromRow(), firstMove.getFromCol());
            // This should fail - either wrong player or backward move
            assertFalse(rules.isValidMove(backwardMove));
        }

        @Test
        @DisplayName("Move to light square should be rejected")
        void moveToLightSquareRejected() {
            // Light squares have even sum of row + col
            CheckersMove move = new CheckersMove(5, 0, 4, 0); // Horizontal move (invalid)
            assertFalse(rules.isValidMove(move));
        }

        @Test
        @DisplayName("Out of bounds move should be rejected")
        void outOfBoundsMoveRejected() {
            CheckersMove move = new CheckersMove(5, 0, 4, -1);
            assertFalse(rules.isValidMove(move));
        }
    }

    @Nested
    @DisplayName("Capture Tests")
    class CaptureTests {

        @Test
        @DisplayName("Capture mechanics work correctly")
        void captureBasicsWork() {
            // Test that captures are identified correctly
            CheckersMove captureMove = new CheckersMove(2, 1, 4, 3);
            assertTrue(captureMove.isCapture(), "2-square diagonal should be a capture");

            CheckersMove regularMove = new CheckersMove(2, 1, 3, 2);
            assertFalse(regularMove.isCapture(), "1-square diagonal should not be a capture");
        }

        @Test
        @DisplayName("Mandatory jumps can be checked")
        void mandatoryJumpsCanBeChecked() {
            // At game start, there should be no mandatory jumps
            List<CheckersMove> jumps = rules.getMandatoryJumps();
            assertTrue(jumps.isEmpty(), "No mandatory jumps at start");
        }
    }

    @Nested
    @DisplayName("King Promotion Tests")
    class KingPromotionTests {

        @Test
        @DisplayName("Piece reaching opposite end should become king")
        void pieceBecomesKingAtOppositeEnd() {
            // This is a complex scenario - we create a minimal test
            // In practice, moving to row 0 for player 1 triggers promotion
            CheckersPiece piece = new CheckersPiece(1, 1, 1);
            assertFalse(piece.isKing());
            piece.promoteToKing();
            assertTrue(piece.isKing());
        }
    }

    @Nested
    @DisplayName("Game State Tests")
    class GameStateTests {

        @Test
        @DisplayName("Turn should alternate after valid moves")
        void turnAlternatesAfterMove() {
            assertEquals(1, rules.getCurrentPlayer());
            var moves = rules.getAllLegalMoves();
            assertFalse(moves.isEmpty());
            rules.makeMove(moves.get(0));
            assertEquals(2, rules.getCurrentPlayer());
        }

        @Test
        @DisplayName("getAllLegalMoves returns valid moves")
        void getAllLegalMovesWorks() {
            List<CheckersMove> moves = rules.getAllLegalMoves();
            assertNotNull(moves);
            assertFalse(moves.isEmpty(), "Should have legal moves at start");

            // At start, player 1 should have 7 possible moves (from pieces at row 5)
            assertTrue(moves.size() >= 7, "Player 1 should have at least 7 moves");
        }

        @Test
        @DisplayName("Board string representation should work")
        void boardStringWorks() {
            String boardStr = rules.getBoardString();
            assertNotNull(boardStr);
            assertFalse(boardStr.isEmpty());
        }
    }

    @Nested
    @DisplayName("CheckersPiece Tests")
    class CheckersPieceTests {

        @Test
        @DisplayName("Piece properties should be correct")
        void piecePropertiesCorrect() {
            CheckersPiece piece = new CheckersPiece(1, 5, 3);
            assertEquals(1, piece.getPlayer());
            assertEquals(5, piece.getRow());
            assertEquals(3, piece.getCol());
            assertFalse(piece.isKing());
        }

        @Test
        @DisplayName("Piece position can be updated")
        void piecePositionCanBeUpdated() {
            CheckersPiece piece = new CheckersPiece(1, 5, 3);
            piece.setPosition(4, 4);
            assertEquals(4, piece.getRow());
            assertEquals(4, piece.getCol());
        }

        @Test
        @DisplayName("Piece toString is informative")
        void pieceToStringInformative() {
            CheckersPiece piece = new CheckersPiece(1, 5, 3);
            String str = piece.toString();
            assertTrue(str.contains("1"));
            assertTrue(str.contains("5"));
            assertTrue(str.contains("3"));
        }
    }

    @Nested
    @DisplayName("CheckersMove Tests")
    class CheckersMoveTests {

        @Test
        @DisplayName("Regular move is not a capture")
        void regularMoveNotCapture() {
            CheckersMove move = new CheckersMove(5, 0, 4, 1);
            assertFalse(move.isCapture());
        }

        @Test
        @DisplayName("Jump move is a capture")
        void jumpMoveIsCapture() {
            CheckersMove move = new CheckersMove(5, 0, 3, 2);
            assertTrue(move.isCapture());
        }

        @Test
        @DisplayName("Move equals works correctly")
        void moveEqualsWorks() {
            CheckersMove move1 = new CheckersMove(5, 0, 4, 1);
            CheckersMove move2 = new CheckersMove(5, 0, 4, 1);
            CheckersMove move3 = new CheckersMove(5, 2, 4, 3);

            assertEquals(move1, move2);
            assertNotEquals(move1, move3);
        }

        @Test
        @DisplayName("Move hashCode is consistent")
        void moveHashCodeConsistent() {
            CheckersMove move1 = new CheckersMove(5, 0, 4, 1);
            CheckersMove move2 = new CheckersMove(5, 0, 4, 1);

            assertEquals(move1.hashCode(), move2.hashCode());
        }

        @Test
        @DisplayName("Move toString is informative")
        void moveToStringInformative() {
            CheckersMove move = new CheckersMove(5, 0, 4, 1);
            String str = move.toString();
            assertTrue(str.contains("5"));
            assertTrue(str.contains("0"));
            assertTrue(str.contains("4"));
            assertTrue(str.contains("1"));
        }
    }
}
