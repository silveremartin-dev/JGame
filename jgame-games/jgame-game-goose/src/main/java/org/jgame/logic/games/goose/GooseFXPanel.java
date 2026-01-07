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
package org.jgame.logic.games.goose;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Tooltip;
import org.jgame.util.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX panel for Game of the Goose.
 * Renders the 63-tile board linearly with wrapping.
 *
 * @author Google Gemini (Antigravity)
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class GooseFXPanel extends BorderPane {

    private final GooseRules rules;
    private final FlowPane boardPane;
    private final Label statusLabel;
    private final List<StackPane> tilePanes;

    private static final int TILE_SIZE = 80;

    public GooseFXPanel(GooseRules rules) {
        this.rules = rules;
        this.boardPane = new FlowPane();
        this.statusLabel = new Label(I18n.get("goose.status.start"));
        this.tilePanes = new ArrayList<>();

        setupUI();
        render(); // Initial render
    }

    private void setupUI() {
        // Board area
        ScrollPane scrollPane = new ScrollPane();
        boardPane.setPadding(new Insets(20));
        boardPane.setHgap(10);
        boardPane.setVgap(10);
        boardPane.setPrefWrapLength(1000); // Width before wrapping
        boardPane.setStyle("-fx-background-color: #f0f0f0;");
        boardPane.setAlignment(Pos.CENTER);

        // Initialize 63 tiles
        for (int i = 1; i <= 63; i++) {
            StackPane tile = createTile(i);
            tilePanes.add(tile);
            boardPane.getChildren().add(tile);
        }

        scrollPane.setContent(boardPane);
        scrollPane.setFitToWidth(true);
        setCenter(scrollPane);

        // Controls
        HBox controls = new HBox(20);
        controls.setPadding(new Insets(15));
        controls.setAlignment(Pos.CENTER);
        controls.setStyle(
                "-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, -5);");

        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        statusLabel.getStyleClass().add("current-player");

        Button rollBtn = new Button(I18n.get("goose.action.roll"));
        rollBtn.getStyleClass().add("roll-dice-button");
        rollBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20;");
        rollBtn.setOnAction(e -> handleRoll());

        Button newGameBtn = new Button(I18n.get("goose.action.new_game"));
        newGameBtn.setOnAction(e -> startNewGame());

        controls.getChildren().addAll(statusLabel, rollBtn, newGameBtn);
        setBottom(controls);
    }

    private StackPane createTile(int number) {
        StackPane tile = new StackPane();
        tile.setPrefSize(TILE_SIZE, TILE_SIZE);

        Rectangle bg = new Rectangle(TILE_SIZE, TILE_SIZE);
        bg.setArcWidth(10);
        bg.setArcHeight(10);

        // Special tiles coloring
        if (number == 63)
            bg.setFill(Color.GOLD);
        else if (number % 9 == 0)
            bg.setFill(Color.LIGHTPINK); // Goose tiles approx logic? (Rules say multi of 9)
        else if (number == 58)
            bg.setFill(Color.BLACK); // Death
        else if (number == 19)
            bg.setFill(Color.LIGHTBLUE); // Inn
        else if (number == 31)
            bg.setFill(Color.LIGHTGRAY); // Well
        else if (number == 52)
            bg.setFill(Color.DARKGRAY); // Prison
        else if (number == 42)
            bg.setFill(Color.ORANGE); // Labyrinth
        else
            bg.setFill(Color.WHITE);

        String tooltipKey = switch (number) {
            case 63 -> "goose.tile.finish";
            case 58 -> "goose.tile.death";
            case 19 -> "goose.tile.inn";
            case 31 -> "goose.tile.well";
            case 52 -> "goose.tile.prison";
            case 42 -> "goose.tile.labyrinth";
            case 6, 12 -> "goose.tile.bridge"; // Assuming bridge logic
            default -> (number % 9 == 0) ? "goose.tile.goose" : null;
        };

        if (tooltipKey != null) {
            Tooltip.install(tile, new Tooltip(I18n.get(tooltipKey)));
        }

        bg.setStroke(Color.GRAY);

        Label numLbl = new Label(String.valueOf(number));
        numLbl.setFont(Font.font("System", FontWeight.BOLD, 18));
        if (number == 58 || number == 52)
            numLbl.setTextFill(Color.WHITE);

        tile.getChildren().addAll(bg, numLbl);
        return tile;
    }

    private void handleRoll() {
        if (!rules.isFinished()) {
            // Rules.nextTurn() handles rolling logic internally if inGameState is CAN_MOVE
            rules.nextTurn();
            render();

            // Update status
            if (rules.isFinished()) {
                statusLabel.setText(I18n.format("goose.status.win", rules.getWinner().getName()));
            } else {
                int currentPlayer = rules.getTurnIndex() + 1; // 1-based usually
                statusLabel.setText(I18n.format("goose.status.turn", currentPlayer));
            }
        }
    }

    private void startNewGame() {
        rules.startGame();
        render();
        statusLabel.setText(I18n.get("goose.status.start"));
    }

    private void render() {
        // Clear previous player positions (keep tiles)
        for (StackPane tile : tilePanes) {
            // Remove circles (keep rect and label)
            tile.getChildren().removeIf(node -> node instanceof Circle);
        }

        // Draw players
        // Rules stores positions in playerPositions array (via internal access logic
        // needed? Accessors exist)
        // GooseRules has getPlayerPosition(index)

        int numPlayers = rules.getPlayers().size();
        for (int i = 0; i < numPlayers; i++) {
            int pos = rules.getPlayerPosition(i);
            if (pos >= 1 && pos <= 63) {
                // Find tile (list is 0-indexed, pos is 1-based)
                StackPane tile = tilePanes.get(pos - 1);

                Circle playerToken = new Circle(10);
                playerToken.setStroke(Color.WHITE);
                playerToken.setStrokeWidth(2);

                // Color based on index
                Color c = switch (i) {
                    case 0 -> Color.RED;
                    case 1 -> Color.BLUE;
                    case 2 -> Color.GREEN;
                    case 3 -> Color.YELLOW;
                    default -> Color.PURPLE;
                };
                playerToken.setFill(c);

                // Offset multiple players on same tile?
                // Minimal offset logic
                playerToken.setTranslateX((i % 2 == 0 ? -10 : 10));
                playerToken.setTranslateY((i > 1 ? 10 : -10));

                tile.getChildren().add(playerToken);
            }
        }
    }
}
