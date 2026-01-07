package org.jgame.client;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

public class GameConfigDialog extends Dialog<GameConfigDialog.GameConfig> {

    public record GameConfig(String p1Type, String p2Type, String aiLevel) {
    }

    public GameConfigDialog(String gameName) {
        setTitle(gameName + " Configuration");
        setHeaderText("Configure Local Game");
        initModality(Modality.APPLICATION_MODAL);

        ButtonType playButtonType = new ButtonType("Play", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(playButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> p1Type = new ComboBox<>();
        p1Type.getItems().addAll("Human", "AI (Random)", "AI (Minimax)");
        p1Type.setValue("Human");

        ComboBox<String> p2Type = new ComboBox<>();
        p2Type.getItems().addAll("Human", "AI (Random)", "AI (Minimax)");
        p2Type.setValue("Human"); // Default to Human vs Human

        ComboBox<String> aiLevel = new ComboBox<>();
        aiLevel.getItems().addAll("Easy", "Medium", "Hard");
        aiLevel.setValue("Medium");
        aiLevel.setDisable(true); // Enable only if AI is selected

        // Logic to enable/disable AI level
        p1Type.valueProperty().addListener((obs, oldVal, newVal) -> updateAiLevelState(p1Type, p2Type, aiLevel));
        p2Type.valueProperty().addListener((obs, oldVal, newVal) -> updateAiLevelState(p1Type, p2Type, aiLevel));

        grid.add(new Label("Player 1 (White/Start):"), 0, 0);
        grid.add(p1Type, 1, 0);
        grid.add(new Label("Player 2 (Black/Next):"), 0, 1);
        grid.add(p2Type, 1, 1);
        grid.add(new Label("AI Difficulty:"), 0, 2);
        grid.add(aiLevel, 1, 2);

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == playButtonType) {
                return new GameConfig(p1Type.getValue(), p2Type.getValue(), aiLevel.getValue());
            }
            return null;
        });
    }

    private void updateAiLevelState(ComboBox<String> p1, ComboBox<String> p2, ComboBox<String> level) {
        boolean hasAi = p1.getValue().contains("AI") || p2.getValue().contains("AI");
        level.setDisable(!hasAi);
    }
}
