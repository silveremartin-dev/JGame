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
package org.jgame.logic.games.solitaire;

import javafx.geometry.Insets;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.jgame.parts.cards.Card;

import java.util.List;

/**
 * JavaFX panel for Solitaire (Klondike).
 * Renders Tableaus, Foundations, and Deck using card images.
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class SolitaireFXPanel extends BorderPane {

    private final SolitaireRules rules;
    private final Pane gameArea;
    private final Label statusLabel;

    // UI Constants
    private static final double CARD_WIDTH = 80;
    private static final double CARD_HEIGHT = 120;
    private static final double MARGIN = 20;

    public SolitaireFXPanel(SolitaireRules rules) {
        this.rules = rules;
        this.gameArea = new Pane();
        this.gameArea.getStyleClass().add("game-area");
        this.statusLabel = new Label("Solitaire - Click Deck to Deal");
        this.statusLabel.getStyleClass().add("status-label");

        setupUI();
        render();
    }

    private void setupUI() {
        gameArea.setStyle("-fx-background-color: #2e8b57;"); // Classic Green
        gameArea.setPrefSize(800, 600);
        setCenter(gameArea);

        // Status bar
        HBox statusBar = new HBox(20);
        statusBar.setPadding(new Insets(10));
        statusBar.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        statusLabel.setTextFill(Color.WHITE);

        Button newGameBtn = new Button("New Game");
        newGameBtn.getStyleClass().add("new-game-button");
        newGameBtn.setOnAction(e -> {
            rules.initializeGame();
            render();
            statusLabel.setText("New Game Started");
        });

        // Stats labels
        Label scoreLabel = new Label("Score: 0");
        scoreLabel.getStyleClass().add("score-label");
        scoreLabel.setTextFill(Color.WHITE);

        Label timerLabel = new Label("00:00");
        timerLabel.getStyleClass().add("timer-label");
        timerLabel.setTextFill(Color.WHITE);

        Button undoBtn = new Button("Undo");
        undoBtn.getStyleClass().add("undo-button");

        Button hintBtn = new Button("Hint");
        hintBtn.getStyleClass().add("hint-button");

        Button statsBtn = new Button("Stats");
        statsBtn.getStyleClass().add("statistics-button");

        Button autoCompleteBtn = new Button("Auto-Complete");
        autoCompleteBtn.getStyleClass().add("auto-complete");
        autoCompleteBtn.setVisible(false); // Only visible when possible

        hintBtn.setOnAction(e -> {
            SolitaireSolver solver = new SolitaireSolver();
            org.jgame.logic.engine.GameAction hint = solver.computeMove(rules.toGameState());
            String moveText;
            if (hint != null) {
                moveText = "Hint: " + hint.actionType();
                if (hint.parameters().containsKey("from")) {
                    moveText += " from " + hint.parameters().get("from");
                }
                if (hint.parameters().containsKey("to")) {
                    moveText += " to " + hint.parameters().get("to");
                }
            } else {
                moveText = "No moves found!";
            }
            Label hintIndicator = new Label(moveText);
            hintIndicator.getStyleClass().add("hint-indicator");
            hintIndicator.setStyle("-fx-background-color: yellow; -fx-padding: 5;");
            gameArea.getChildren().add(hintIndicator);

            // Auto-remove hint after 3 seconds
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.seconds(3));
            pause.setOnFinished(ev -> gameArea.getChildren().remove(hintIndicator));
            pause.play();
        });

        statsBtn.setOnAction(e -> {
            VBox statsDialog = new VBox(new Label("Statistics"));
            statsDialog.getStyleClass().add("statistics-dialog");
            gameArea.getChildren().add(statsDialog);
        });

        autoCompleteBtn.setOnAction(e -> {
            rules.autoComplete();
            render();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        statusBar.getChildren().addAll(statusLabel, scoreLabel, timerLabel, spacer, undoBtn, hintBtn, statsBtn,
                autoCompleteBtn, newGameBtn);
        setBottom(statusBar);

        // Dynamic UI updates
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), ev -> {
                    scoreLabel.setText("Score: " + rules.getScore(null).getScoreValue());
                    long secs = rules.getElapsedTime();
                    timerLabel.setText(String.format("%02d:%02d", secs / 60, secs % 60));
                    autoCompleteBtn.setVisible(rules.canAutoComplete());
                }));
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
    }

    void render() {
        gameArea.getChildren().clear();

        // Render Deck (Top Left)
        if (!rules.getDeck().isEmpty()) {
            // Using logic to determine back or just a colored rectangle if not found
            // Since we can't easily check for file existence dynamically without
            // exceptions,
            // we will use a Rectangle styled as a card back.
            javafx.scene.Node deckBack = createCardBack();
            deckBack.getStyleClass().add("stock-pile");
            deckBack.setLayoutX(MARGIN);
            deckBack.setLayoutY(MARGIN);
            // Click to draw
            deckBack.setOnMouseClicked(e -> {
                rules.drawToWaste();
                render();
            });
            gameArea.getChildren().add(deckBack);
        } else {
            // Empty deck placeholder
            Rectangle emptyDeck = new Rectangle(CARD_WIDTH, CARD_HEIGHT, Color.TRANSPARENT);
            emptyDeck.getStyleClass().add("stock-pile");
            emptyDeck.setStroke(Color.LIGHTGRAY);
            emptyDeck.setArcWidth(10);
            emptyDeck.setArcHeight(10);
            emptyDeck.setLayoutX(MARGIN);
            emptyDeck.setLayoutY(MARGIN);
            // Click to reset? Rules.drawToWaste handles reset if empty
            emptyDeck.setOnMouseClicked(e -> {
                rules.drawToWaste();
                render();
            });
            gameArea.getChildren().add(emptyDeck);
        }

        // Render Waste (Right of Deck)
        double wasteX = MARGIN + CARD_WIDTH + MARGIN;

        // Base placeholder for waste
        Rectangle wastePlaceholder = new Rectangle(CARD_WIDTH, CARD_HEIGHT, Color.TRANSPARENT);
        wastePlaceholder.getStyleClass().add("waste-pile");
        wastePlaceholder.setStroke(Color.LIGHTGRAY);
        wastePlaceholder.setStrokeWidth(1);
        wastePlaceholder.getStrokeDashArray().addAll(5d, 5d);
        wastePlaceholder.setArcWidth(10);
        wastePlaceholder.setArcHeight(10);
        wastePlaceholder.setLayoutX(wasteX);
        wastePlaceholder.setLayoutY(MARGIN);
        gameArea.getChildren().add(wastePlaceholder);

        if (!rules.getWaste().isEmpty()) {
            Card topWaste = rules.getWaste().peek();

            javafx.scene.Node wasteNode;
            Image img = getCardImage(topWaste);
            if (img != null) {
                ImageView iv = new ImageView(img);
                iv.setFitWidth(CARD_WIDTH);
                iv.setFitHeight(CARD_HEIGHT);
                wasteNode = iv;
            } else {
                wasteNode = createCardPlaceholder(topWaste);
            }

            wasteNode.getStyleClass().add("waste-pile");
            wasteNode.getStyleClass().add("card");
            wasteNode.setLayoutX(wasteX);
            wasteNode.setLayoutY(MARGIN);
            gameArea.getChildren().add(wasteNode);
        }

        // Render Foundations (Top Right)
        double startFoundX = wasteX + CARD_WIDTH + MARGIN * 3; // Gap
        for (int i = 0; i < 4; i++) {
            double x = startFoundX + (i * (CARD_WIDTH + MARGIN));

            // Base placeholder
            Rectangle placeHolder = new Rectangle(CARD_WIDTH, CARD_HEIGHT, Color.TRANSPARENT);
            placeHolder.getStyleClass().add("foundation-pile");
            placeHolder.setStroke(Color.LIGHTGREEN);
            placeHolder.setStrokeWidth(2);
            placeHolder.setArcWidth(10);
            placeHolder.setArcHeight(10);
            placeHolder.setLayoutX(x);
            placeHolder.setLayoutY(MARGIN);
            gameArea.getChildren().add(placeHolder);

            if (!rules.getFoundations().get(i).isEmpty()) {
                Card top = rules.getFoundations().get(i).peek();

                javafx.scene.Node cardNode;
                Image img = getCardImage(top);
                if (img != null) {
                    ImageView iv = new ImageView(img);
                    iv.setFitWidth(CARD_WIDTH);
                    iv.setFitHeight(CARD_HEIGHT);
                    cardNode = iv;
                } else {
                    cardNode = createCardPlaceholder(top);
                }
                cardNode.getStyleClass().add("foundation-pile");
                cardNode.getStyleClass().add("card");
                cardNode.setLayoutX(x);
                cardNode.setLayoutY(MARGIN);
                gameArea.getChildren().add(cardNode);
            }
        }

        // Render Tableaus (Bottom)
        double startTabY = MARGIN + CARD_HEIGHT + MARGIN;
        for (int i = 0; i < 7; i++) {
            double x = MARGIN + (i * (CARD_WIDTH + MARGIN));
            double y = startTabY;

            List<Card> stack = rules.getTableaus().get(i);
            if (stack.isEmpty()) {
                Rectangle emptyCol = new Rectangle(CARD_WIDTH, CARD_HEIGHT, Color.TRANSPARENT);
                emptyCol.getStyleClass().add("tableau-pile");
                emptyCol.getStyleClass().add("tableau-pile-" + (i + 1));
                emptyCol.setStroke(Color.DARKGREEN);
                emptyCol.setLayoutX(x);
                emptyCol.setLayoutY(y);
                gameArea.getChildren().add(emptyCol);
            } else {
                for (int j = 0; j < stack.size(); j++) {
                    Card c = stack.get(j);
                    // Cascade
                    double cardY = y + (j * 20); // 20px offset

                    if (c.isFaceUp()) {
                        javafx.scene.Node cardNode;
                        Image img = getCardImage(c);
                        if (img != null) {
                            ImageView iv = new ImageView(img);
                            iv.setFitWidth(CARD_WIDTH);
                            iv.setFitHeight(CARD_HEIGHT);
                            cardNode = iv;
                        } else {
                            cardNode = createCardPlaceholder(c);
                        }
                        cardNode.getStyleClass().add("tableau-pile");
                        cardNode.getStyleClass().add("tableau-pile-" + (i + 1));
                        cardNode.getStyleClass().add("card");
                        cardNode.getStyleClass().add("card-face-up");
                        cardNode.setLayoutX(x);
                        cardNode.setLayoutY(cardY);
                        gameArea.getChildren().add(cardNode);
                    } else {
                        javafx.scene.Node back = createCardBack();
                        back.getStyleClass().add("tableau-pile");
                        back.getStyleClass().add("tableau-pile-" + (i + 1));
                        back.getStyleClass().add("card");
                        back.getStyleClass().add("card-face-down");
                        back.setLayoutX(x);
                        back.setLayoutY(cardY);
                        gameArea.getChildren().add(back);
                    }
                }
            }
        }

        if (rules.isFinished()) {
            Label winLabel = new Label("VICTORY!");
            winLabel.setStyle(
                    "-fx-font-size: 60px; -fx-text-fill: gold; -fx-effect: dropshadow(gaussian, black, 10, 0, 0, 0);");
            gameArea.getChildren().add(winLabel);
            winLabel.layoutXProperty().bind(gameArea.widthProperty().subtract(winLabel.widthProperty()).divide(2));
            winLabel.layoutYProperty().bind(gameArea.heightProperty().subtract(winLabel.heightProperty()).divide(2));
        }
    }

    // Helper to get image path
    public Image getCardImage(Card card) {
        if (!card.isFaceUp())
            return null; // Should use createCardBack

        String rankStr = switch (card.getRank()) {
            case ACE -> "ace";
            case JACK -> "jack";
            case QUEEN -> "queen";
            case KING -> "king";
            default -> String.valueOf(card.getRank().getValue());
        };

        String suitStr = switch (card.getSuit()) {
            case CLUBS -> "clubs";
            case DIAMONDS -> "diamonds";
            case HEARTS -> "hearts";
            case SPADES -> "spades";
        };

        String filename = rankStr + "_of_" + suitStr + ".png";
        String path = "/images/cards/" + filename;
        try {
            if (getClass().getResource(path) != null) {
                return new Image(getClass().getResource(path).toExternalForm());
            }
        } catch (Exception e) {
            // ignore
        }
        return null; // Fallback to placeholder if image missing
    }

    private javafx.scene.Node createCardBack() {
        Rectangle back = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        back.setArcWidth(10);
        back.setArcHeight(10);
        // Nice gradient for back
        javafx.scene.paint.Stop[] stops = new javafx.scene.paint.Stop[] {
                new javafx.scene.paint.Stop(0, Color.DARKBLUE),
                new javafx.scene.paint.Stop(1, Color.ROYALBLUE)
        };
        javafx.scene.paint.LinearGradient lg = new javafx.scene.paint.LinearGradient(
                0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE, stops);
        back.setFill(lg);
        back.setStroke(Color.WHITE);
        back.setStrokeWidth(2);

        StackPane p = new StackPane(back);
        p.getStyleClass().add("card-back");
        return p;
    }

    private javafx.scene.Node createCardPlaceholder(Card card) {
        StackPane p = new StackPane();
        p.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        Rectangle bg = new Rectangle(CARD_WIDTH, CARD_HEIGHT, Color.WHITE);
        bg.setArcWidth(10);
        bg.setArcHeight(10);
        bg.setStroke(Color.BLACK);

        Label l = new Label(card.toString());
        p.getChildren().addAll(bg, l);
        return p;
    }
}
