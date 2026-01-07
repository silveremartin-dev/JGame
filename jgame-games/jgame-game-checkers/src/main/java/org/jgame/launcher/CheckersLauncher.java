package org.jgame.launcher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jgame.logic.games.checkers.CheckersFXPanel;
import org.jgame.logic.games.checkers.CheckersRules;

public class CheckersLauncher extends Application {

    @Override
    public void start(Stage primaryStage) {
        CheckersRules rules = new CheckersRules();
        rules.initGame();
        // Default to Human vs Human for standalone launcher
        rules.addPlayer(new org.jgame.model.GameUser("Red"));
        rules.addPlayer(new org.jgame.model.GameUser("Black"));

        CheckersFXPanel panel = new CheckersFXPanel(rules);
        Scene scene = new Scene(panel, 800, 650);

        primaryStage.setTitle("JGame Checkers - Standalone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
