package org.jgame.launcher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jgame.logic.games.chess.ChessFXPanel;
import org.jgame.logic.games.chess.ChessRules;

public class ChessLauncher extends Application {

    @Override
    public void start(Stage primaryStage) {
        ChessRules rules = new ChessRules();
        rules.initializeGame();
        // Default to Human vs Human for standalone launcher
        rules.addPlayer(new org.jgame.model.GameUser("White"));
        rules.addPlayer(new org.jgame.model.GameUser("Black"));

        ChessFXPanel panel = new ChessFXPanel(rules);
        Scene scene = new Scene(panel, 800, 650);

        primaryStage.setTitle("JGame Chess - Standalone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
