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
package org.jgame.ui.test;

import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.TimeoutException;

/**
 * Base class for JavaFX UI tests using TestFX and Monocle.
 * 
 * <p>
 * This class provides common setup and teardown for headless JavaFX testing.
 * All game UI tests should extend this class.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Google Gemini (Antigravity)
 * @version 2.0
 * @since 2.0
 */
public abstract class BaseUITest extends ApplicationTest {

    protected Stage stage;

    @BeforeAll
    public static void setUpHeadless() {
        // Configure headless mode for CI/CD environments
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        setupStage(stage);
        stage.show();
    }

    /**
     * Override this method to set up your specific stage/scene.
     * 
     * @param stage the primary stage
     * @throws Exception if setup fails
     */
    protected abstract void setupStage(Stage stage) throws Exception;

    @BeforeEach
    public void setUpTest() throws Exception {
        // Additional setup if needed
    }

    @AfterEach
    public void tearDownTest() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    /**
     * Helper method to wait for a specific duration.
     * 
     * @param milliseconds duration to wait
     */
    protected void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
