/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.logic.games.chess;

/**
 * Bishop piece.
 */
public record Bishop(ChessPiece.Color color) implements ChessPiece {
    @Override
    public char getSymbol() {
        return 'B';
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    public ChessPiece.Color getColor() {
        return color;
    }
}
