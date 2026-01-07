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
import javafx.scene.input.*;
import javafx.scene.control.Tooltip;
import org.jgame.parts.cards.Card;
import org.jgame.util.I18n;

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

    // Selection tracking
    private int selectedType = -1; // 0=Waste, 1=Tableau, 2=Foundation
    private int selectedPileIdx = -1;
    private int selectedCardIdx = -1;

    // UI Constants
    private static final double CARD_WIDTH = 80;
    private static final double CARD_HEIGHT = 120;
    private static final double MARGIN = 20;

    public SolitaireFXPanel(SolitaireRules rules) {
        this.rules = rules;
        this.gameArea = new Pane();
        this.gameArea.getStyleClass().add("game-area");
        this.statusLabel = new Label(I18n.get("solitaire.status.deal"));
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

        Button newGameBtn = new Button(I18n.get("solitaire.action.new_game"));
        newGameBtn.getStyleClass().add("new-game-button");
        newGameBtn.setOnAction(e -> {
            rules.initializeGame();
            render();
            statusLabel.setText(I18n.get("solitaire.status.new_game"));
        });

        // Stats labels
        Label scoreLabel = new Label(I18n.format("solitaire.score", 0));
        scoreLabel.getStyleClass().add("score-label");
        scoreLabel.setTextFill(Color.WHITE);

        Label timerLabel = new Label("00:00");
        timerLabel.getStyleClass().add("timer-label");
        timerLabel.setTextFill(Color.WHITE);

        Button undoBtn = new Button(I18n.get("solitaire.action.undo"));
        undoBtn.getStyleClass().add("undo-button");

        Button hintBtn = new Button(I18n.get("solitaire.action.hint"));
        hintBtn.getStyleClass().add("hint-button");

        Button statsBtn = new Button(I18n.get("solitaire.action.stats"));
        statsBtn.getStyleClass().add("statistics-button");

        Button autoCompleteBtn = new Button(I18n.get("solitaire.action.auto_complete"));
        autoCompleteBtn.getStyleClass().add("auto-complete");
        autoCompleteBtn.setVisible(false); // Only visible when possible

        hintBtn.setOnAction(e -> {
            SolitaireSolver solver = new SolitaireSolver();
            org.jgame.logic.engine.GameAction hint = solver.computeMove(rules.toGameState());
            String moveText;
            if (hint != null) {
                moveText = I18n.get("solitaire.action.hint") + ": " + hint.actionType();
                if (hint.parameters().containsKey("from")) {
                    moveText += " from " + hint.parameters().get("from");
                }
                if (hint.parameters().containsKey("to")) {
                    moveText += " to " + hint.parameters().get("to");
                }
            } else {
                moveText = I18n.get("solitaire.hint.no_moves");
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
            VBox statsDialog = new VBox(new Label(I18n.get("solitaire.stats.title")));
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
                    scoreLabel.setText(I18n.format("solitaire.score", rules.getScore(null).getScoreValue()));
                    long secs = rules.getElapsedTime();
                    timerLabel.setText(String.format("%02d:%02d", (int) (secs / 60), (int) (secs % 60)));
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
            Tooltip.install(deckBack, new Tooltip(I18n.get("solitaire.pile.stock")));
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
            Tooltip.install(emptyDeck, new Tooltip(I18n.get("solitaire.pile.stock")));
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
        Tooltip.install(wastePlaceholder, new Tooltip(I18n.get("solitaire.pile.waste")));
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
            if (selectedType == 0) {
                wasteNode.getStyleClass().add("selected");
                wasteNode.setStyle("-fx-effect: dropshadow(three-pass-box, yellow, 10, 0, 0, 0);");
            }
            wasteNode.setLayoutX(wasteX);
            wasteNode.setLayoutY(MARGIN);

            wasteNode.setOnMouseClicked(e -> handlePileClick(0, 0, rules.getWaste().size() - 1));
            setupDragSource(wasteNode, 0, 0, rules.getWaste().size() - 1);
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
            Tooltip.install(placeHolder, new Tooltip(I18n.get("solitaire.pile.foundation")));
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
                if (selectedType == 2 && selectedPileIdx == i) {
                    cardNode.getStyleClass().add("selected");
                    cardNode.setStyle("-fx-effect: dropshadow(three-pass-box, yellow, 10, 0, 0, 0);");
                }
                cardNode.setLayoutX(x);
                cardNode.setLayoutY(MARGIN);

                final int fIdx = i;
                cardNode.setOnMouseClicked(e -> handlePileClick(2, fIdx, rules.getFoundations().get(fIdx).size() - 1));

                gameArea.getChildren().add(cardNode);
            } else {
                final int fIdx = i;
                placeHolder.setOnMouseClicked(e -> handlePileClick(2, fIdx, -1));
            }
            setupDropTarget(gameArea.getChildren().get(gameArea.getChildren().size() - 1), 2, i);
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
                Tooltip.install(emptyCol, new Tooltip(I18n.get("solitaire.pile.tableau")));
                gameArea.getChildren().add(emptyCol);
                setupDropTarget(emptyCol, 1, i);
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
                        if (selectedType == 1 && selectedPileIdx == i && j >= selectedCardIdx) {
                            cardNode.getStyleClass().add("selected");
                            cardNode.setStyle("-fx-effect: dropshadow(three-pass-box, yellow, 10, 0, 0, 0);");
                        }
                        cardNode.setLayoutX(x);
                        cardNode.setLayoutY(cardY);

                        final int tIdx = i;
                        final int cIdx = j;
                        cardNode.setOnMouseClicked(e -> handlePileClick(1, tIdx, cIdx));
                        setupDragSource(cardNode, 1, tIdx, cIdx);
                        setupDropTarget(cardNode, 1, tIdx);

                        gameArea.getChildren().add(cardNode);
                    } else {
                        javafx.scene.Node back = createCardBack();
                        back.getStyleClass().add("tableau-pile");
                        back.getStyleClass().add("tableau-pile-" + (i + 1));
                        back.getStyleClass().add("card");
                        back.getStyleClass().add("card-face-down");
                        back.setLayoutX(x);
                        back.setLayoutY(cardY);

                        final int tIdx = i;
                        final int cIdx = j;
                        back.setOnMouseClicked(e -> {
                            if (cIdx == stack.size() - 1) {
                                rules.flipTableauCard(tIdx);
                                render();
                            }
                        });
                        gameArea.getChildren().add(back);
                    }
                }
            }
        }

        if (rules.isFinished()) {
            Label winLabel = new Label(I18n.get("solitaire.status.victory"));
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

    private void setupDragSource(javafx.scene.Node node, int type, int pileIdx, int cardIdx) {
        node.setOnDragDetected(e -> {
            Dragboard db = node.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(type + ":" + pileIdx + ":" + cardIdx);
            db.setContent(content);

            // Create drag view from the node snapshot to preserve size and scaling
            db.setDragView(node.snapshot(null, null));
            db.setDragViewOffsetX(e.getX());
            db.setDragViewOffsetY(e.getY());
            e.consume();
        });
    }

    private void setupDropTarget(javafx.scene.Node node, int type, int pileIdx) {
        node.setOnDragOver(e -> {
            if (e.getGestureSource() != node && e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        node.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String[] parts = db.getString().split(":");
                int srcType = Integer.parseInt(parts[0]);
                int srcPileIdx = Integer.parseInt(parts[1]);
                int srcCardIdx = Integer.parseInt(parts[2]);

                // Reuse handlePileClick logic indirectly or directly?
                // Better to call the same logic as the click
                success = executeMove(srcType, srcPileIdx, srcCardIdx, type, pileIdx);
            }
            e.setDropCompleted(success);
            if (success)
                render();
            e.consume();
        });
    }

    private boolean executeMove(int srcType, int srcPileIdx, int srcCardIdx, int targetType, int targetPileIdx) {
        boolean success = false;
        switch (srcType) {
            case 0 -> { // From Waste
                if (targetType == 1)
                    success = rules.moveWasteToTableau(targetPileIdx);
                else if (targetType == 2)
                    success = rules.moveWasteToFoundation(targetPileIdx);
            }
            case 1 -> { // From Tableau
                if (targetType == 1)
                    success = rules.moveTableauToTableau(srcPileIdx, srcCardIdx, targetPileIdx);
                else if (targetType == 2 && srcCardIdx == rules.getTableaus().get(srcPileIdx).size() - 1)
                    success = rules.moveTableauToFoundation(srcPileIdx, targetPileIdx);
            }
            case 2 -> { // From Foundation
                if (targetType == 1)
                    success = rules.moveFoundationToTableau(srcPileIdx, targetPileIdx);
            }
        }
        if (success) {
            statusLabel.setText(I18n.get("solitaire.status.move_success"));
        } else {
            statusLabel.setText(I18n.get("solitaire.status.move_invalid"));
        }
        return success;
    }

    private void handlePileClick(int type, int pileIdx, int cardIdx) {
        if (selectedType == -1) {
            // Select
            if (cardIdx != -1) {
                selectedType = type;
                selectedPileIdx = pileIdx;
                selectedCardIdx = cardIdx;
            }
        } else {
            // Move
            executeMove(selectedType, selectedPileIdx, selectedCardIdx, type, pileIdx);

            // Clear selection
            selectedType = -1;
            selectedPileIdx = -1;
            selectedCardIdx = -1;
        }
        render();
    }
}
