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
import org.jgame.logic.games.checkers.CheckersMove;
import org.jgame.logic.games.checkers.CheckersPiece;
import org.jgame.logic.games.checkers.CheckersRules;
import org.jgame.plugin.ui.GamePanel;

import java.awt.*;

/**
 * Game panel for Checkers visualization.
 */
public class CheckersPanel extends GamePanel {

    private final CheckersRules checkersRules;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public CheckersPanel(GameInterface game) {
        super(game);
        if (!(game instanceof CheckersRules)) {
            throw new IllegalArgumentException("Game must be CheckersRules");
        }
        this.checkersRules = (CheckersRules) game;
    }

    @Override
    protected void renderGame(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        int squareSize = Math.min(width, height) / 8;
        int boardSize = squareSize * 8;
        int offsetX = (width - boardSize) / 2;
        int offsetY = (height - boardSize) / 2;

        // Draw board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int x = offsetX + col * squareSize;
                int y = offsetY + row * squareSize;

                // Checkers board pattern (dark squares are where pieces go)
                boolean isLight = (row + col) % 2 == 0;
                g2d.setColor(isLight ? new Color(240, 217, 181) : new Color(118, 74, 52)); // Wood colors
                g2d.fillRect(x, y, squareSize, squareSize);

                // Highlight selected square
                if (row == selectedRow && col == selectedCol) {
                    g2d.setColor(new Color(255, 255, 0, 100)); // Yellow highlight
                    g2d.fillRect(x, y, squareSize, squareSize);
                }

                // Draw piece
                CheckersPiece piece = checkersRules.getPiece(row, col);
                if (piece != null) {
                    drawPiece(g2d, piece, x, y, squareSize);
                }
            }
        }

        // Draw status
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String status = checkersRules.isFinished()
                ? "Game Over - Winner: "
                        + (checkersRules.getWinner() != null ? checkersRules.getWinner().getName() : "None")
                : "Turn: Player " + checkersRules.getCurrentPlayer();
        g2d.drawString(status, 20, 30);
    }

    private void drawPiece(Graphics2D g2d, CheckersPiece piece, int x, int y, int size) {
        int padding = size / 8;
        int pieceSize = size - (padding * 2);

        // Player 1 = Red (or White), Player 2 = Black
        // Standard US Checkers: P1=Red/Black, P2=White/Red. Let's use Red vs Black.
        Color pieceColor = (piece.getPlayer() == 1) ? Color.RED : Color.BLACK;
        Color outlineColor = (piece.getPlayer() == 1) ? new Color(150, 0, 0) : new Color(50, 50, 50);

        g2d.setColor(pieceColor);
        g2d.fillOval(x + padding, y + padding, pieceSize, pieceSize);

        g2d.setColor(outlineColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x + padding, y + padding, pieceSize, pieceSize);

        // Draw King indicator
        if (piece.isKing()) {
            g2d.setColor(Color.YELLOW); // Crown color
            g2d.setFont(new Font("Arial", Font.BOLD, size / 2));
            FontMetrics fm = g2d.getFontMetrics();
            String crown = "K";
            int textX = x + (size - fm.stringWidth(crown)) / 2;
            int textY = y + (size + fm.getAscent()) / 2 - 5;
            g2d.drawString(crown, textX, textY);
        }
    }

    @Override
    protected void handleMouseClick(int x, int y) {
        int width = getWidth();
        int height = getHeight();
        int squareSize = Math.min(width, height) / 8;
        int boardSize = squareSize * 8;
        int offsetX = (width - boardSize) / 2;
        int offsetY = (height - boardSize) / 2;

        int col = (x - offsetX) / squareSize;
        int row = (y - offsetY) / squareSize;

        if (row < 0 || row > 7 || col < 0 || col > 7)
            return;

        if (selectedRow == -1) {
            // Select piece
            CheckersPiece piece = checkersRules.getPiece(row, col);
            if (piece != null && piece.getPlayer() == checkersRules.getCurrentPlayer()) {
                selectedRow = row;
                selectedCol = col;
            }
        } else {
            // Try move
            // Check if clicking on same piece to deselect
            if (row == selectedRow && col == selectedCol) {
                selectedRow = -1;
                selectedCol = -1;
            } else {
                // Attempt move
                CheckersMove move = new CheckersMove(selectedRow, selectedCol, row, col);
                if (checkersRules.isValidMove(move)) {
                    checkersRules.makeMove(move);
                    selectedRow = -1;
                    selectedCol = -1;
                } else {
                    // Invalid move, maybe selecting a different piece?
                    CheckersPiece piece = checkersRules.getPiece(row, col);
                    if (piece != null && piece.getPlayer() == checkersRules.getCurrentPlayer()) {
                        selectedRow = row;
                        selectedCol = col;
                    } else {
                        // Clicked empty square but invalid move -> deselect
                        selectedRow = -1;
                        selectedCol = -1;
                    }
                }
            }
        }

        updateDisplay();
    }
}
