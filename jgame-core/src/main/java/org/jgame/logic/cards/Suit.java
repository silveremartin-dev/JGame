package org.jgame.logic.cards;

/**
 * Standard playing card suits.
 */
public enum Suit {
    HEARTS("Hearts", "♥", Color.RED),
    DIAMONDS("Diamonds", "♦", Color.RED),
    CLUBS("Clubs", "♣", Color.BLACK),
    SPADES("Spades", "♠", Color.BLACK);

    private final String name;
    private final String symbol;
    private final Color color;

    Suit(String name, String symbol, Color color) {
        this.name = name;
        this.symbol = symbol;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public Color getColor() {
        return color;
    }
}
