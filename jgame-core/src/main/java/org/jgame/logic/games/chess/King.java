/*
 * MIT License
 *
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.logic.games.chess;

/**
 * King piece.
 */
public record King(ChessPiece.Color color) implements ChessPiece {
    @Override
    public char getSymbol() {
        return 'K';
    }

    @Override
    public int getValue() {
        return 0;
    } // King is priceless

    @Override
    public ChessPiece.Color getColor() {
        return color;
    }
}
