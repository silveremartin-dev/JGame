/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.logic.games.chess;

/**
 * Knight piece.
 */
public record Knight(ChessPiece.Color color) implements ChessPiece {
    @Override
    public char getSymbol() {
        return 'N';
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
