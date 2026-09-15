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
import org.jgame.model.GameUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class ClientDiscoveryTest {

    @Start
    public void start(Stage stage) {
        // Dummy start to init JavaFX toolkit
        stage.show();
    }

    @Test
    public void testGameInstantiation() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                // Verify Chess
                ChessRules chessRules = new ChessRules();
                chessRules.initializeGame();
                ChessFXPanel chessPanel = new ChessFXPanel(chessRules);
                assertNotNull(chessPanel);

                // Verify Checkers
                CheckersRules checkersRules = new CheckersRules();
                checkersRules.initGame();
                CheckersFXPanel checkersPanel = new CheckersFXPanel(checkersRules);
                assertNotNull(checkersPanel);

                // Verify Goose (needs at least 2 players)
                GooseRules gooseRules = new GooseRules();
                gooseRules.addPlayer(new GameUser("Player 1"));
                gooseRules.addPlayer(new GameUser("Player 2"));
                gooseRules.startGame();
                GooseFXPanel goosePanel = new GooseFXPanel(gooseRules);
                assertNotNull(goosePanel);

                // Verify Solitaire
                SolitaireRules solitaireRules = new SolitaireRules();
                solitaireRules.initializeGame();
                SolitaireFXPanel solitairePanel = new SolitaireFXPanel(solitaireRules);
                assertNotNull(solitairePanel);
            } catch (Throwable t) {
                error.set(t);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "JavaFX initialization timed out");
        if (error.get() != null) {
            fail("Exception during game UI initialization", error.get());
        }
    }
}
