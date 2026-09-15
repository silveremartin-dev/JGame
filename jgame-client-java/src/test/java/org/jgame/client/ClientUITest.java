package org.jgame.client;

import javafx.scene.layout.FlowPane;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jgame.ui.test.BaseUITest;

public class ClientUITest extends BaseUITest {

    private JGameApp app;

    @Override
    protected void setupStage(Stage stage) throws Exception {
        app = new JGameApp();
        app.start(stage);
    }

    @Test
    public void testLanguageDiscoveryAndSwitch() {
        // Navigate to Options tab
        clickOn("⚙️ Options");

        // Find language combo
        ComboBox<String> langBox = lookup(".combo-box").queryComboBox();
        Assertions.assertNotNull(langBox);
        Assertions.assertFalse(langBox.getItems().isEmpty());

        // Test switching language
        interact(() -> langBox.setValue("Français"));
        Assertions.assertDoesNotThrow(() -> clickOn("⚙️ Options"));
    }

    @Test
    public void testOfflineModeDetection() {
        // Navigate to Games tab
        clickOn("🎲 Games");

        // Wait for async refresh to complete (Offline mode fallback)
        try {
            Thread.sleep(5000); // 5s to ensure offline fallback triggers
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify Status Label exists
        javafx.scene.control.Label statusLabel = lookup("#statusLabel").query();
        Assertions.assertNotNull(statusLabel, "Status label should exist");
        String text = statusLabel.getText();

        // Log the actual status for debugging
        System.out.println("Status Label Text: " + text);

        // Verify grid is populated
        FlowPane grid = lookup("#gameGrid").query();
        Assertions.assertNotNull(grid, "Game grid should exist");
        Assertions.assertFalse(grid.getChildren().isEmpty(), "Game grid should have children");
    }
}
