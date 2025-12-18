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

package org.jgame.plugin.ui;

import org.jgame.logic.engine.GameRules;

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
 */
public abstract class GamePanel extends JPanel {

    /** The game rules this panel visualizes */
    protected final GameRules rules;

    /** Preferred panel size */
    protected static final Dimension DEFAULT_SIZE = new Dimension(600, 600);

    /**
     * Creates a new game panel for the given rules.
     * 
     * @param rules the game rules to visualize
     * @throws IllegalArgumentException if rules is null
     */
    protected GamePanel(GameRules rules) {
        if (rules == null) {
            throw new IllegalArgumentException("Game rules cannot be null");
        }
        this.rules = rules;

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
     * Gets the game rules associated with this panel.
     * 
     * @return game rules instance
     */
    public GameRules getRules() {
        return rules;
    }
}
