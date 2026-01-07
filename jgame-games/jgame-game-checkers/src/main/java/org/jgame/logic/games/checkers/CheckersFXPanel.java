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
package org.jgame.logic.games.checkers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.jgame.ai.GameAI;
import org.jgame.ai.RandomAI;
import org.jgame.logic.engine.GameAction;
import org.jgame.model.GameUser;
import org.jgame.parts.PlayerInterface;
import org.jgame.parts.players.GamePlayer;
import org.jgame.utils.SoundManager;

/**
 * JavaFX panel for Checkers game.
 * Uses Reversi images for pieces (because they look like draughts).
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class CheckersFXPanel extends BorderPane {

    private static final int SQUARE_SIZE = 60;
    private static final int BOARD_SIZE = 8;

    private final CheckersRules rules;
    private final GridPane boardGrid;
    private final Label statusLabel;

    private int selectedRow = -1;
    private int selectedCol = -1;

    public CheckersFXPanel(CheckersRules rules) {
        this.rules = rules;
        this.boardGrid = new GridPane();
        this.statusLabel = new Label("Player 1's turn");

        setupUI();
        render();
    }

    private void setupUI() {
        this.boardGrid.getStyleClass().add("grid-pane");
        this.boardGrid.setAlignment(Pos.CENTER);
        this.boardGrid.setStyle("-fx-background-color: #2c3e50; -fx-padding: 20;");

        // Initialize grid squares
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane square = new StackPane();
                square.setPrefSize(SQUARE_SIZE, SQUARE_SIZE);
                square.setId("square-" + row + "-" + col);

                int r = row;
                int c = col;
                square.setOnMouseClicked(e -> handleClick(r, c));

                boardGrid.add(square, col, row);
            }
        }
        setCenter(boardGrid);

        // Status bar at bottom
        HBox statusBar = new HBox(20);
        statusBar.setPadding(new Insets(15));
        statusBar.setStyle("-fx-background-color: rgba(0,0,0,0.2);");

        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        statusLabel.setStyle("-fx-text-fill: white;");

        Button newGameBtn = new Button("New Game");
        newGameBtn.setOnAction(e -> newGame());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        statusBar.getChildren().addAll(statusLabel, spacer, newGameBtn);
        setBottom(statusBar);
    }

    private void handleClick(int row, int col) {
        if (rules.isFinished())
            return;

        // Prevent interaction if it's an AI's turn
        PlayerInterface currentPlayer = rules.getPlayers().get(rules.getCurrentPlayer() - 1);
        if (currentPlayer instanceof GamePlayer gp && gp.getUser().getPlayerType() == GameUser.PlayerType.ARTIFICIAL) {
            return;
        }

        if (selectedRow == -1) {
            // Select piece
            CheckersPiece piece = rules.getPiece(row, col);
            if (piece != null && piece.getPlayer() == rules.getCurrentPlayer()) {
                selectedRow = row;
                selectedCol = col;
                updateStatus("Selected (" + row + "," + col + ")");
                render();
            }
        } else {
            // Move
            CheckersMove move = new CheckersMove(selectedRow, selectedCol, row, col);
            if (rules.isValidMove(move)) {
                // Execute move with animation
                animateMove(move, () -> {
                    CheckersBoard boardBefore = rules.getCheckersBoard().copy();
                    boolean turnComplete = rules.makeMove(move);

                    // Play sounds
                    if (rules.isFinished()) {
                        SoundManager.getInstance().playWin();
                    } else {
                        // Check if a piece was captured (board changed at intermediate squares for
                        // jump)
                        if (isCapture(boardBefore, rules.getCheckersBoard(), move)) {
                            SoundManager.getInstance().playCapture();
                        } else {
                            SoundManager.getInstance().playMove();
                        }
                    }

                    if (!turnComplete) {
                        updateStatus("Multi-jump available! Continue jumping.");
                        // Keep selection on the piece at new position
                        selectedRow = row;
                        selectedCol = col;
                    } else {
                        selectedRow = -1;
                        selectedCol = -1;
                        updateStatus("Player " + rules.getCurrentPlayer() + "'s turn");
                    }
                    render();
                    checkAndTriggerAI();
                });
                return;
            } else {
                // Deselect or select new piece
                CheckersPiece piece = rules.getPiece(row, col);
                if (piece != null && piece.getPlayer() == rules.getCurrentPlayer()) {
                    selectedRow = row;
                    selectedCol = col;
                    updateStatus("Selected (" + row + "," + col + ")");
                } else {
                    selectedRow = -1;
                    selectedCol = -1;
                    updateStatus("Invalid move.");
                }
            }
            render();
            checkAndTriggerAI();
        }
    }

    private boolean isCapture(CheckersBoard before, CheckersBoard after, CheckersMove move) {
        // Simple heuristic: if the number of pieces decreased, it's a capture.
        return countPieces(before) > countPieces(after);
    }

    private int countPieces(CheckersBoard board) {
        int count = 0;
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (board.getPiece(r, c) != null)
                    count++;
            }
        }
        return count;
    }

    private void animateMove(CheckersMove move, Runnable onFinished) {
        StackPane fromSquare = getSquare(move.getFromRow(), move.getFromCol());
        if (fromSquare != null && !fromSquare.getChildren().isEmpty()) {
            // Find the ImageView (might be wrapped or have a Label for King)
            javafx.scene.Node pieceNode = null;
            for (javafx.scene.Node node : fromSquare.getChildren()) {
                if (node instanceof ImageView) {
                    pieceNode = node;
                    break;
                }
            }

            if (pieceNode != null) {
                final javafx.scene.Node finalPieceNode = pieceNode;
                double deltaX = (move.getToCol() - move.getFromCol()) * SQUARE_SIZE;
                double deltaY = (move.getToRow() - move.getFromRow()) * SQUARE_SIZE;

                TranslateTransition transition = new TranslateTransition(Duration.millis(300), finalPieceNode);
                transition.setByX(deltaX);
                transition.setByY(deltaY);
                transition.setOnFinished(e -> {
                    finalPieceNode.setTranslateX(0);
                    finalPieceNode.setTranslateY(0);
                    onFinished.run();
                });
                transition.play();
                return;
            }
        }
        onFinished.run();
    }

    private StackPane getSquare(int row, int col) {
        for (javafx.scene.Node node : boardGrid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (StackPane) node;
            }
        }
        return null;
    }

    private void render() {
        for (javafx.scene.Node node : boardGrid.getChildren()) {
            if (node instanceof StackPane square) {
                int col = GridPane.getColumnIndex(square);
                int row = GridPane.getRowIndex(square);

                // Background color (Dark/Light)
                boolean isDark = (row + col) % 2 != 0;
                String color = isDark ? "#b58863" : "#f0d9b5";

                if (row == selectedRow && col == selectedCol) {
                    color = "#7fc97f";
                } else if (rules.getPiece(row, col) == null && selectedRow != -1) {
                    // Highlight valid moves? (Optional optimization)
                    CheckersMove tryMove = new CheckersMove(selectedRow, selectedCol, row, col);
                    if (rules.isValidMove(tryMove)) {
                        color = "#a9ff99";
                    }
                }

                square.setStyle("-fx-background-color: " + color + ";");
                square.getChildren().clear();

                // Draw piece
                CheckersPiece piece = rules.getPiece(row, col);
                if (piece != null) {
                    ImageView pieceView = createPieceImage(piece);
                    if (pieceView != null) {
                        pieceView.setFitWidth(SQUARE_SIZE - 10);
                        pieceView.setFitHeight(SQUARE_SIZE - 10);
                        square.getChildren().add(pieceView);

                        // King marker
                        if (piece.isKing()) {
                            // Add a gold crown or simple marker.
                            // Since we don't have a separate image, we'll use a visual indicator.
                            // A small circle in the center or a border.
                            Label kingMark = new Label("K");
                            kingMark.setFont(Font.font("System", FontWeight.BOLD, 24));
                            kingMark.setTextFill(piece.getPlayer() == 1 ? Color.BLACK : Color.WHITE);
                            square.getChildren().add(kingMark);
                        }
                    }
                }
            }
        }

        if (rules.isFinished()) {
            statusLabel.setText("Game Over! Winner: Player " + rules.getWinnerId());
        }
    }

    private ImageView createPieceImage(CheckersPiece piece) {
        String imageName;
        String styleClass;
        // Player 1 = White/Red (Rows 0-2). Player 2 = Black (Rows 5-7).
        // Reversi images: OO (Light/White), XX (Dark/Black).
        if (piece.getPlayer() == 1) {
            imageName = "Reversi_OOt45.svg.png";
            styleClass = "checkers-piece-red";
        } else {
            imageName = "Reversi_XXt45.svg.png";
            styleClass = "checkers-piece-black";
        }

        String path = "/images/pieces/reversi/" + imageName;
        try {
            if (getClass().getResource(path) != null) {
                ImageView view = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
                view.getStyleClass().add(styleClass);
                return view;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void newGame() {
        rules.initGame();
        selectedRow = -1;
        selectedCol = -1;
        render();
        updateStatus("New Game Started. Player 1's turn.");
        checkAndTriggerAI();
    }

    private void updateStatus(String msg) {
        statusLabel.setText(msg);
    }

    private void checkAndTriggerAI() {
        if (rules.isFinished())
            return;

        PlayerInterface currentPlayer = rules.getPlayers().get(rules.getCurrentPlayer() - 1);
        if (currentPlayer instanceof GamePlayer gp && gp.getUser().getPlayerType() == GameUser.PlayerType.ARTIFICIAL) {
            String aiType = gp.getUser().getLogin();
            GameAI ai = aiType.equals("AI_MINIMAX") ? new CheckersMinimaxAI(3) : new RandomAI();

            updateStatus(ai.getName() + " is thinking...");

            Task<GameAction> aiTask = new Task<>() {
                @Override
                protected GameAction call() {
                    return ai.computeMove(rules.toGameState());
                }
            };

            aiTask.setOnSucceeded(event -> {
                GameAction action = aiTask.getValue();
                if (action != null) {
                    Platform.runLater(() -> {
                        int fromRow = (int) action.parameters().get("fromRow");
                        int fromCol = (int) action.parameters().get("fromCol");
                        int toRow = (int) action.parameters().get("toRow");
                        int toCol = (int) action.parameters().get("toCol");
                        CheckersMove move = new CheckersMove(fromRow, fromCol, toRow, toCol);

                        animateMove(move, () -> {
                            rules.executeAction(currentPlayer, action);
                            render();
                            checkAndTriggerAI();
                        });
                    });
                }
            });

            new Thread(aiTask).start();
        }
    }
}
