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
package org.jgame.plugin.ui;

import org.jgame.logic.GameInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Abstract base class for game-specific UI panels.
 * 
 * <p>
 * Each game plugin must provide a concrete implementation of this class
 * that handles rendering and user interaction for that specific game.
 * </p>
 * 
 * <h2>Responsibilities:</h2>
 * <ul>
 * <li>Render game board</li>
 * <li>Render game pieces</li>
 * <li>Handle mouse input for moves</li>
 * <li>Display game status</li>
 * <li>Update display when game state changes</li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 * @author Google Gemini (Antigravity)
 */
public abstract class GamePanel extends JPanel {

    /** The game instance this panel visualizes */
    protected final GameInterface game;

    /** Preferred panel size */
    protected static final Dimension DEFAULT_SIZE = new Dimension(600, 600);

    /**
     * Creates a new game panel for the given game.
     * 
     * @param game the game to visualize
     * @throws IllegalArgumentException if game is null
     */
    protected GamePanel(GameInterface game) {
        if (game == null) {
            throw new IllegalArgumentException("Game instance cannot be null");
        }
        this.game = game;

        setPreferredSize(DEFAULT_SIZE);
        setBackground(Color.WHITE);
        setDoubleBuffered(true);

        // Add mouse listener for game interaction
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    /**
     * Paints the game panel.
     * 
     * <p>
     * This method calls {@link #renderGame(Graphics2D)} for actual rendering.
     * </p>
     */
    @Override
    protected final void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smooth graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        renderGame(g2d);
    }

    /**
     * Renders the game board and pieces.
     * 
     * <p>
     * Subclasses must implement this to draw their specific game.
     * </p>
     * 
     * @param g2d graphics context for rendering
     */
    protected abstract void renderGame(Graphics2D g2d);

    /**
     * Handles mouse click events.
     * 
     * <p>
     * Subclasses should implement this to process user input and
     * make game moves based on where the user clicked.
     * </p>
     * 
     * @param x mouse X coordinate
     * @param y mouse Y coordinate
     */
    protected abstract void handleMouseClick(int x, int y);

    /**
     * Updates the display to reflect current game state.
     * 
     * <p>
     * This should be called after any game state change to refresh
     * the visual representation.
     * </p>
     */
    public void updateDisplay() {
        repaint();
    }

    /**
     * Gets the game instance associated with this panel.
     * 
     * @return game instance
     */
    public GameInterface getGame() {
        return game;
    }
}
