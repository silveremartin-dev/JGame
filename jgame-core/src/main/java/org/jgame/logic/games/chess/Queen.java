/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.logic.games.chess;

/**
 * Queen piece.
 */
public record Queen(ChessPiece.Color color) implements ChessPiece {
    @Override
    public char getSymbol() {
        return 'Q';
    }

    @Override
    public int getValue() {
        return 9;
    }

    @Override
    public ChessPiece.Color getColor() {
        return color;
    }
}
