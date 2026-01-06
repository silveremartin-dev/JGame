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
package org.jgame.plugin;

import org.jgame.logic.GameInterface;
import org.jgame.plugin.ui.GamePanel;

/**
 * Interface for game plugins.
 * 
 * <p>
 * All games in the system must implement this interface to be loadable
 * as plugins. The plugin system allows dynamic game registration and UI
 * creation.
 * </p>
 * 
 * <h2>Implementation Requirements:</h2>
 * <ul>
 * <li>Provide a {@link GameDescriptor} with game metadata</li>
 * <li>Create {@link GameInterface} instances for game logic</li>
 * <li>Create {@link GamePanel} instances for UI rendering</li>
 * </ul>
 * 
 * <h2>Example Implementation:</h2>
 * 
 * <pre>
 * {
 *     &#64;code
 *     public class GoosePlugin implements GamePlugin {
 *         &#64;Override
 *         public GameDescriptor getDescriptor() {
 *             return new GameDescriptor(
 *                     "goose",
 *                     "Game of the Goose",
 *                     "1.0",
 *                     "JGame Team",
 *                     "Classic board game",
 *                     "Roll dice and move...",
 *                     2, 6,
 *                     Map.of("difficulty", "easy"));
 *         }
 * 
 *         &#64;Override
 *         public GameInterface createRules() {
 *             return new GooseRules();
 *         }
 * 
 *         @Override
 *         public GamePanel createPanel(GameInterface rules) {
 *             return new GoosePanel((GooseRules) rules);
 *         }
 *     }
 * }
 * </pre>
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @author Google Gemini (Antigravity)
 */
public interface GamePlugin {

    /**
     * Gets the game descriptor containing metadata and configuration.
     * 
     * @return game descriptor (never null)
     */
    GameDescriptor getDescriptor();

    /**
     * Creates a new instance of the game logic.
     * 
     * <p>
     * This method is called when starting a new game. It should return
     * a fresh instance of the game in initial state.
     * </p>
     * 
     * @return new game instance (never null)
     */
    GameInterface createRules();

    /**
     * Creates a new game panel for UI rendering.
     * 
     * <p>
     * The panel is responsible for:
     * <ul>
     * <li>Rendering the game board</li>
     * <li>Rendering game pieces</li>
     * <li>Handling mouse input for moves</li>
     * <li>Displaying game status</li>
     * </ul>
     * </p>
     * 
     * @param game the game instance to visualize
     * @return new game panel (never null)
     * @throws IllegalArgumentException if game is null or wrong type
     */
    GamePanel createPanel(GameInterface game);
}
