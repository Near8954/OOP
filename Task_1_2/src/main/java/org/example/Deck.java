package org.example;

import java.util.Collections;
import java.util.Stack;

/**
 * Class for deck.
 */
public class Deck {
    private final Stack<Card> cards = new Stack<>();

    /**
     * Creates deck and fills it with cards.
     */
    public Deck() {
        for (Suits suit : Suits.values()) {
            for (Ranks rank : Ranks.values()) {
                cards.push(new Card(suit, rank));
            }
        }
    }

    /**
     * Shuffles deck.
     */
    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    /**
     * Returns one card.
     *
     */
    public Card getCard() {
        return cards.pop();
    }
}