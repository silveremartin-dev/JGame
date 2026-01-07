package org.jgame.launcher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jgame.logic.games.solitaire.SolitaireFXPanel;
import org.jgame.logic.games.solitaire.SolitaireRules;

public class SolitaireLauncher extends Application {

    @Override
    public void start(Stage primaryStage) {
        SolitaireRules rules = new SolitaireRules();
        rules.initializeGame();

        SolitaireFXPanel panel = new SolitaireFXPanel(rules);
        Scene scene = new Scene(panel, 900, 700);

        primaryStage.setTitle("JGame Solitaire - Standalone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
