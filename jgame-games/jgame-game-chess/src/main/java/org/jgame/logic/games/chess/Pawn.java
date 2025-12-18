/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.logic.games.chess;

/**
 * Pawn piece.
 */
public record Pawn(ChessPiece.Color color) implements ChessPiece {
    @Override
    public char getSymbol() {
        return 'P';
    }

    @Override
    public int getValue() {
        return 1;
    }

    @Override
    public ChessPiece.Color getColor() {
        return color;
    }
}
