/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.logic.games.chess;

/**
 * Rook piece.
 */
public record Rook(ChessPiece.Color color) implements ChessPiece {
    @Override
    public char getSymbol() {
        return 'R';
    }

    @Override
    public int getValue() {
        return 5;
    }

    @Override
    public ChessPiece.Color getColor() {
        return color;
    }
}
