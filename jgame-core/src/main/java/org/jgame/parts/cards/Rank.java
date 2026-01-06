package org.jgame.parts.cards;

/**
 * Standard playing card ranks.
 */
public enum Rank {
    ACE(1, "Ace", "A"),
    TWO(2, "Two", "2"),
    THREE(3, "Three", "3"),
    FOUR(4, "Four", "4"),
    FIVE(5, "Five", "5"),
    SIX(6, "Six", "6"),
    SEVEN(7, "Seven", "7"),
    EIGHT(8, "Eight", "8"),
    NINE(9, "Nine", "9"),
    TEN(10, "Ten", "10"),
    JACK(11, "Jack", "J"),
    QUEEN(12, "Queen", "Q"),
    KING(13, "King", "K");

    private final int value;
    private final String name;
    private final String symbol;

    Rank(int value, String name, String symbol) {
        this.value = value;
        this.name = name;
        this.symbol = symbol;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }
}
