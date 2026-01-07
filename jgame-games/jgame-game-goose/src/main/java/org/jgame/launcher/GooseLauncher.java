package org.jgame.launcher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jgame.logic.games.goose.GooseFXPanel;
import org.jgame.logic.games.goose.GooseRules;

public class GooseLauncher extends Application {

    @Override
    public void start(Stage primaryStage) {
        GooseRules rules = new GooseRules();
        org.jgame.model.GameUser u1 = new org.jgame.model.GameUser("Player 1", "pass");
        u1.setPlayerType(org.jgame.model.GameUser.PlayerType.HUMAN);
        rules.addPlayer(u1);

        org.jgame.model.GameUser u2 = new org.jgame.model.GameUser("Player 2", "pass");
        u2.setPlayerType(org.jgame.model.GameUser.PlayerType.HUMAN);
        rules.addPlayer(u2);

        rules.startGame();

        GooseFXPanel panel = new GooseFXPanel(rules);
        Scene scene = new Scene(panel, 1000, 800);

        primaryStage.setTitle("JGame of the Goose - Standalone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
