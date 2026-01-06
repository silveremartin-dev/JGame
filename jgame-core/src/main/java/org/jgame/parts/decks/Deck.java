package org.jgame.parts.decks;

import org.jgame.parts.cards.Card;
import org.jgame.parts.cards.Rank;
import org.jgame.parts.cards.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Represents a deck of playing cards.
 */
public class Deck extends AbstractDeck {
    private final Stack<Card> cards;

    public Deck() {
        this.cards = new Stack<>();
        initializeDeck();
    }

    private void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.push(new Card(suit, rank));
            }
        }
        syncParent();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
        syncParent();
    }

    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        Card c = cards.pop();
        syncParent();
        return c;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void reset() {
        cards.clear();
        initializeDeck();
        shuffle();
    }

    // Helper to keep AbstractDeck.cards in sync (though redundant, it satisfies
    // contract)
    private void syncParent() {
        super.setCards(new ArrayList<>(cards));
    }

    @Override
    public int size() {
        return cards.size();
    }
}
