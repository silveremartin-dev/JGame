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

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * JavaFX panel for Chess game.
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class ChessFXPanel extends BorderPane {

    private static final int SQUARE_SIZE = 60;
    private static final int BOARD_SIZE = 8;

    private final ChessRules rules;
    private final Canvas boardCanvas;
    private final Label statusLabel;

    private int selectedRow = -1;
    private int selectedCol = -1;

    public ChessFXPanel(ChessRules rules) {
        this.rules = rules;
        this.boardCanvas = new Canvas(SQUARE_SIZE * BOARD_SIZE, SQUARE_SIZE * BOARD_SIZE);
        this.statusLabel = new Label("White to move");

        setupUI();
        setupEventHandlers();
        render();
    }

    private void setupUI() {
        // Board in center
        StackPane boardPane = new StackPane(boardCanvas);
        boardPane.setStyle("-fx-background-color: #2c3e50; -fx-padding: 20;");
        setCenter(boardPane);

        // Status bar at bottom
        HBox statusBar = new HBox(20);
        statusBar.setPadding(new Insets(15));
        statusBar.setStyle("-fx-background-color: rgba(0,0,0,0.2);");

        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        statusLabel.setStyle("-fx-text-fill: white;");

        Button newGameBtn = new Button("New Game");
        newGameBtn.setOnAction(e -> newGame());

        Button resignBtn = new Button("Resign");
        resignBtn.setOnAction(e -> resign());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        statusBar.getChildren().addAll(statusLabel, spacer, newGameBtn, resignBtn);
        setBottom(statusBar);
    }

    private void setupEventHandlers() {
        boardCanvas.setOnMouseClicked(e -> {
            int col = (int) (e.getX() / SQUARE_SIZE);
            int row = (int) (e.getY() / SQUARE_SIZE);
            handleClick(row, col);
        });
    }

    private void handleClick(int row, int col) {
        if (selectedRow == -1) {
            // First click - select piece
            selectedRow = row;
            selectedCol = col;
            updateStatus("Selected: " + toChessNotation(row, col));
        } else {
            // Second click - try move
            String from = toChessNotation(selectedRow, selectedCol);
            String to = toChessNotation(row, col);

            // Validate and execute move using rules
            ChessMove move = new ChessMove(selectedRow, selectedCol, row, col);
            if (rules.isValidMove(move)) {
                if (rules.makeMove(move)) {
                    updateStatus("Moved: " + from + " to " + to);
                } else {
                    updateStatus("Move rejected by rules: " + from + " to " + to);
                }
            } else {
                updateStatus("Invalid move: " + from + " to " + to);
            }

            selectedRow = -1;
            selectedCol = -1;
        }
        render();
    }

    private String toChessNotation(int row, int col) {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }

    private void render() {
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();

        // Draw board squares
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boolean isLight = (row + col) % 2 == 0;
                gc.setFill(isLight ? Color.web("#f0d9b5") : Color.web("#b58863"));

                // Highlight selected square
                if (row == selectedRow && col == selectedCol) {
                    gc.setFill(Color.web("#7fc97f"));
                }

                gc.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }

        // Draw pieces
        gc.setFont(Font.font("System", FontWeight.NORMAL, 42));
        drawInitialPosition(gc);

        // Draw coordinates
        gc.setFont(Font.font("System", 12));
        gc.setFill(Color.WHITE);
        for (int i = 0; i < BOARD_SIZE; i++) {
            // Files (a-h)
            gc.fillText(String.valueOf((char) ('a' + i)), i * SQUARE_SIZE + SQUARE_SIZE / 2 - 4,
                    BOARD_SIZE * SQUARE_SIZE - 4);
            // Ranks (1-8)
            gc.fillText(String.valueOf(8 - i), 4, i * SQUARE_SIZE + SQUARE_SIZE / 2 + 4);
        }
    }

    private void drawInitialPosition(GraphicsContext gc) {
        // Initial chess position
        String[][] board = {
                { "♜", "♞", "♝", "♛", "♚", "♝", "♞", "♜" },
                { "♟", "♟", "♟", "♟", "♟", "♟", "♟", "♟" },
                { "", "", "", "", "", "", "", "" },
                { "", "", "", "", "", "", "", "" },
                { "", "", "", "", "", "", "", "" },
                { "", "", "", "", "", "", "", "" },
                { "♙", "♙", "♙", "♙", "♙", "♙", "♙", "♙" },
                { "♖", "♘", "♗", "♕", "♔", "♗", "♘", "♖" }
        };

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (!board[row][col].isEmpty()) {
                    gc.setFill(Color.BLACK);
                    gc.fillText(board[row][col],
                            col * SQUARE_SIZE + SQUARE_SIZE / 2 - 14,
                            row * SQUARE_SIZE + SQUARE_SIZE / 2 + 14);
                }
            }
        }
    }

    private void newGame() {
        selectedRow = -1;
        selectedCol = -1;
        statusLabel.setText("White to move");
        render();
    }

    private void resign() {
        statusLabel.setText("You resigned!");
    }

    public void updateStatus(String message) {
        statusLabel.setText(message);
    }
}
