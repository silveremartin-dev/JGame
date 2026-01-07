package org.jgame.client;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.jgame.logic.games.checkers.CheckersFXPanel;
import org.jgame.logic.games.checkers.CheckersRules;
import org.jgame.logic.games.chess.ChessFXPanel;
import org.jgame.logic.games.chess.ChessRules;
import org.jgame.logic.games.goose.GooseFXPanel;
import org.jgame.logic.games.goose.GooseRules;
import org.jgame.logic.games.solitaire.SolitaireFXPanel;
import org.jgame.logic.games.solitaire.SolitaireRules;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(ApplicationExtension.class)
public class ClientDiscoveryTest {

    @Start
    public void start(Stage stage) {
        // Just a dummy start to init JavaFX toolkit
        stage.show();
    }

    @Test
    public void testGameInstantiation() {
        Platform.runLater(() -> {
            // Verify Chess
            assertDoesNotThrow(() -> {
                ChessRules chessRules = new ChessRules();
                chessRules.initializeGame();
                ChessFXPanel chessPanel = new ChessFXPanel(chessRules);
                assertNotNull(chessPanel);
            });

            // Verify Checkers
            assertDoesNotThrow(() -> {
                CheckersRules checkersRules = new CheckersRules();
                checkersRules.initGame();
                CheckersFXPanel checkersPanel = new CheckersFXPanel(checkersRules);
                assertNotNull(checkersPanel);
            });

            // Verify Goose
            assertDoesNotThrow(() -> {
                GooseRules gooseRules = new GooseRules();
                gooseRules.startGame();
                GooseFXPanel goosePanel = new GooseFXPanel(gooseRules);
                assertNotNull(goosePanel);
            });

            // Verify Solitaire
            assertDoesNotThrow(() -> {
                SolitaireRules solitaireRules = new SolitaireRules();
                solitaireRules.initializeGame();
                SolitaireFXPanel solitairePanel = new SolitaireFXPanel(solitaireRules);
                assertNotNull(solitairePanel);
            });
        });
    }
}
