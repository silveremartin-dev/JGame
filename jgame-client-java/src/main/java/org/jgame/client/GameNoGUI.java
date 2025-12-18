/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
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
 *
 * Enhanced with AI assistance from Google Gemini (Antigravity)
 */

package org.jgame.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Headless (no GUI) game client for running games from command line.
 * 
 * <p>
 * Provides console-based game execution for automated testing,
 * AI training, and server-side game processing.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class GameNoGUI {

    private static final Logger LOGGER = LogManager.getLogger(GameNoGUI.class);

    private boolean running;

    /**
     * Creates a new headless game client.
     */
    public GameNoGUI() {
        this.running = false;
        LOGGER.info("Headless game client initialized");
    }

    /**
     * Starts the headless game client.
     * 
     * <p>
     * Currently provides basic console output. Full implementation
     * will include game loop, AI integration, and server communication.
     * </p>
     */
    public void start() {
        LOGGER.info("Starting headless game client");
        this.running = true;

        // Basic headless execution - logs status and waits for commands
        System.out.println("JGame Headless Client Started");
        System.out.println("Use --help for available commands");

        // Placeholder for future game loop
        LOGGER.info("Headless client ready for game commands");
    }

    /**
     * Stops the headless game client.
     */
    public void stop() {
        LOGGER.info("Stopping headless game client");
        this.running = false;
    }

    /**
     * Checks if the client is running.
     * 
     * @return true if running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }
}
