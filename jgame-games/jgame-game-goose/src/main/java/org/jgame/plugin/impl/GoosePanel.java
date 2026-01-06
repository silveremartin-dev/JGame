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
package org.jgame.plugin.impl;

import org.jgame.logic.GameInterface;
import org.jgame.logic.games.goose.GooseRules;
import org.jgame.plugin.ui.GamePanel;

import java.awt.*;

/**
 * Game panel for visualizing Game of the Goose.
 * 
 * <p>
 * This is a minimal implementation showing the spiral board layout
 * and basic rendering. Future enhancements will add mouse control and
 * better graphics.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class GoosePanel extends GamePanel {

    private final GooseRules gooseRules;

    /**
     * Creates a new Goose panel.
     * 
     * @param game the goose game instance
     */
    public GoosePanel(GameInterface game) {
        super(game);
        if (!(game instanceof GooseRules)) {
            throw new IllegalArgumentException("Game must be GooseRules");
        }
        this.gooseRules = (GooseRules) game;
    }

    @Override
    protected void renderGame(Graphics2D g2d) {
        // Simple rendering - spiral board with 63 squares
        int width = getWidth();
        int height = getHeight();

        // Draw background
        g2d.setColor(new Color(240, 230, 220));
        g2d.fillRect(0, 0, width, height);

        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Game of the Goose", 20, 40);

        // Draw simple spiral (63 squares in a 9x7 grid)
        int squareSize = 60;
        int startX = 50;
        int startY = 80;

        for (int i = 0; i < 63; i++) {
            int row = i / 9;
            int col = i % 9;

            int x = startX + col * squareSize;
            int y = startY + row * squareSize;

            // Draw square
            g2d.setColor(Color.WHITE);
            g2d.fillRect(x, y, squareSize - 2, squareSize - 2);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, squareSize - 2, squareSize - 2);

            // Draw square number
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(String.valueOf(i + 1), x + 5, y + 15);
        }

        // Draw game status
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Players: " + gooseRules.getPlayers().size(), 20, height - 40);
        g2d.drawString("Status: " + (gooseRules.isFinished() ? "Finished" : "In Progress"),
                20, height - 20);
    }

    @Override
    protected void handleMouseClick(int x, int y) {
        // Execute next turn when clicking on the board
        // nextTurn() handles dice rolling and player movement internally
        if (!gooseRules.isFinished()) {
            gooseRules.nextTurn();
        }

        updateDisplay();
    }

}
