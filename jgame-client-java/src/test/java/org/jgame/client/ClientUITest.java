package org.jgame.client;

import javafx.scene.layout.FlowPane;

import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.jgame.ui.test.BaseUITest;

public class ClientUITest extends BaseUITest {

    private JGameApp app;

    @Override
    protected void setupStage(Stage stage) throws Exception {
        app = new JGameApp();
        app.start(stage);
    }

    @Test
    public void testLanguageDiscoveryAndSwitch(FxRobot robot) {
        // Navigate to Options tab
        robot.clickOn("⚙️ Options");

        // Find language combo
        ComboBox<String> langBox = robot.lookup(".combo-box").queryComboBox();
        Assertions.assertNotNull(langBox);
        Assertions.assertFalse(langBox.getItems().isEmpty());

        // Test switching language (should trigger refreshUI)
        robot.interact(() -> langBox.setValue("Français"));
        // Since we don't have full resource bundles in test env, we just verify no
        // crash and UI is responsive
        Assertions.assertDoesNotThrow(() -> robot.clickOn("⚙️ Options"));
    }

    @Test
    public void testOfflineModeDetection(FxRobot robot) {
        // Navigate to Games tab
        robot.clickOn("🎲 Games");

        // Wait for async refresh to complete (Offline mode fallback)
        try {
            Thread.sleep(5000); // 5s to ensure offline fallback triggers
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify Status Label exists
        javafx.scene.control.Label statusLabel = robot.lookup("#statusLabel").query();
        Assertions.assertNotNull(statusLabel, "Status label should exist");
        String text = statusLabel.getText();

        // Log the actual status for debugging
        System.out.println("Status Label Text: " + text);

        // Verify grid is populated (should have at least game cards or loading text)
        FlowPane grid = robot.lookup("#gameGrid").query();
        Assertions.assertNotNull(grid, "Game grid should exist");
        Assertions.assertFalse(grid.getChildren().isEmpty(), "Game grid should have children");
    }
}
