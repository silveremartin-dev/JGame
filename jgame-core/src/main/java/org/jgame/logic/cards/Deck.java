package org.jgame.logic.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents a deck of playing cards.
 */
public class Deck {
    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        initializeStandardDeck();
    }

    private void initializeStandardDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Optional<Card> draw() {
        if (cards.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(cards.remove(cards.size() - 1));
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void reset() {
        cards.clear();
        initializeStandardDeck();
    }
}
