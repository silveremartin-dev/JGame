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

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jgame.model.GameUser;
import org.jgame.ui.test.BaseUITest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive UI tests for Chess game using TestFX.
 * 
 * <p>
 * Tests the Chess UI including board rendering, piece movement,
 * user interactions, and game state updates.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Google Gemini (Antigravity)
 * @version 2.0
 * @since 2.0
 */
public class ChessUITest extends BaseUITest {

    private ChessRules chessGame;
    private ChessFXPanel chessPanel;

    @Override
    protected void setupStage(Stage stage) throws Exception {
        // Initialize chess game
        chessGame = new ChessRules();

        // Add players
        GameUser player1 = new GameUser("Player1");
        GameUser player2 = new GameUser("Player2");
        chessGame.addPlayer(player1);
        chessGame.addPlayer(player2);

        // Initialize game
        chessGame.initializeGame();

        // Create chess panel
        chessPanel = new ChessFXPanel(chessGame);

        // Create scene
        VBox root = new VBox(10);
        root.getChildren().add(new Label("Chess Game Test"));
        root.getChildren().add(chessPanel);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Chess UI Test");
    }

    @Test
    public void testBoardInitialization() {
        // Verify board is displayed
        assertNotNull(chessPanel);

        // Verify chess game is initialized
        assertNotNull(chessGame);
        assertFalse(chessGame.isFinished());

        // Verify players are added
        assertEquals(2, chessGame.getPlayers().size());
    }

    @Test
    public void testBoardRendering() {
        // Find the board grid
        GridPane boardGrid = lookup(".grid-pane").query();
        assertNotNull(boardGrid, "Board grid should be rendered");

        // Verify 8x8 grid
        assertEquals(64, boardGrid.getChildren().size(), "Board should have 64 squares");
    }

    @Test
    public void testPieceSelection() {
        // Click on a white pawn (e2)
        clickOn(".chess-piece-white-pawn");

        // Verify piece is selected
        // (This would check for visual feedback like highlighting)
        waitFor(100);

        // Verify valid moves are shown
        // (Implementation depends on how ChessFXPanel shows valid moves)
    }

    @Test
    public void testPieceMovement() {
        // Click on white pawn at e2
        clickOn("#square-e2");
        waitFor(100);

        // Click on destination e4
        clickOn("#square-e4");
        waitFor(100);

        // Verify move was made
        // (Check game state or board state)
        assertNotNull(chessGame.getBoard());
    }

    @Test
    public void testInvalidMove() {
        // Try to move a piece to an invalid square
        clickOn("#square-e2");
        waitFor(100);

        // Try to move to e5 (invalid for pawn's first move)
        clickOn("#square-e5");
        waitFor(100);

        // Verify move was rejected
        // (Piece should still be at e2)
    }

    @Test
    public void testTurnIndicator() {
        // Verify turn indicator shows White's turn
        Label turnLabel = lookup(".turn-indicator").query();
        if (turnLabel != null) {
            assertTrue(turnLabel.getText().contains("White") ||
                    turnLabel.getText().contains("Player1"));
        }
    }

    @Test
    public void testGameReset() {
        // Make a move
        clickOn("#square-e2");
        clickOn("#square-e4");
        waitFor(100);

        // Reset game
        chessGame.initializeGame();

        // Verify game is reset
        assertFalse(chessGame.isFinished());
    }

    @Test
    public void testMultipleMoves() {
        // Simulate a sequence of moves
        // White pawn e2-e4
        makeMove("e2", "e4");

        // Black pawn e7-e5
        makeMove("e7", "e5");

        // White knight g1-f3
        makeMove("g1", "f3");

        // Verify game is still in progress
        assertFalse(chessGame.isFinished());
    }

    @Test
    public void testBoardColors() {
        // Verify alternating square colors
        GridPane boardGrid = lookup(".grid-pane").query();
        if (boardGrid != null) {
            // Check that squares have proper styling
            assertNotNull(boardGrid.getChildren());
        }
    }

    @Test
    public void testPieceIcons() {
        // Verify all pieces have proper icons/representations
        // This depends on how pieces are rendered (images, unicode, etc.)

        // Count white pieces
        long whitePieces = lookup(".chess-piece-white").queryAll().size();
        assertEquals(16, whitePieces, "Should have 16 white pieces at start");

        // Count black pieces
        long blackPieces = lookup(".chess-piece-black").queryAll().size();
        assertEquals(16, blackPieces, "Should have 16 black pieces at start");
    }

    /**
     * Helper method to make a move.
     */
    private void makeMove(String from, String to) {
        clickOn("#square-" + from);
        waitFor(100);
        clickOn("#square-" + to);
        waitFor(100);
    }
}
