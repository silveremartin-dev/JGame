/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.plugin.impl;

import org.jgame.logic.games.chess.*;
import org.jgame.plugin.ui.GamePanel;

import java.awt.*;

/**
 * Game panel for Chess visualization.
 */
public class ChessPanel extends GamePanel {

    private final ChessRules chessRules;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public ChessPanel(ChessRules rules) {
        super(rules);
        this.chessRules = rules;
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

                // Checkerboard pattern
                boolean isLight = (row + col) % 2 == 0;
                g2d.setColor(isLight ? new Color(240, 217, 181) : new Color(181, 136, 99));
                g2d.fillRect(x, y, squareSize, squareSize);

                // Highlight selected square
                if (row == selectedRow && col == selectedCol) {
                    g2d.setColor(new Color(255, 255, 0, 100));
                    g2d.fillRect(x, y, squareSize, squareSize);
                }

                // Draw piece
                ChessPiece piece = chessRules.getBoard().getPiece(row, col);
                if (piece != null) {
                    drawPiece(g2d, piece, x, y, squareSize);
                }
            }
        }

        // Draw status
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String status = chessRules.isFinished()
                ? "Game Over - Winner: " + (chessRules.getWinner() != null ? chessRules.getWinner().getLogin() : "Draw")
                : "Turn: " + chessRules.getCurrentTurn();
        g2d.drawString(status, 20, 30);
    }

    private void drawPiece(Graphics2D g2d, ChessPiece piece, int x, int y, int size) {
        String symbol = switch (piece) {
            case Pawn _ -> "♙";
            case Knight _ -> "♘";
            case Bishop _ -> "♗";
            case Rook _ -> "♖";
            case Queen _ -> "♕";
            case King _ -> "♔";
        };

        // Use filled symbols for black
        if (piece.getColor() == ChessPiece.Color.BLACK) {
            symbol = switch (piece) {
                case Pawn _ -> "♟";
                case Knight _ -> "♞";
                case Bishop _ -> "♝";
                case Rook _ -> "♜";
                case Queen _ -> "♛";
                case King _ -> "♚";
            };
        }

        g2d.setColor(piece.getColor() == ChessPiece.Color.WHITE ? Color.WHITE : Color.BLACK);
        g2d.setFont(new Font("Serif", Font.PLAIN, size * 3 / 4));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = x + (size - fm.stringWidth(symbol)) / 2;
        int textY = y + (size + fm.getAscent()) / 2 - 5;

        // Shadow for visibility
        g2d.setColor(piece.getColor() == ChessPiece.Color.WHITE ? Color.BLACK : Color.WHITE);
        g2d.drawString(symbol, textX + 1, textY + 1);

        g2d.setColor(piece.getColor() == ChessPiece.Color.WHITE ? Color.WHITE : Color.BLACK);
        g2d.drawString(symbol, textX, textY);
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
            ChessPiece piece = chessRules.getBoard().getPiece(row, col);
            if (piece != null && piece.getColor() == chessRules.getCurrentTurn()) {
                selectedRow = row;
                selectedCol = col;
            }
        } else {
            // Try move
            ChessMove move = new ChessMove(selectedRow, selectedCol, row, col);
            chessRules.makeMove(move);
            selectedRow = -1;
            selectedCol = -1;
        }

        updateDisplay();
    }
}
