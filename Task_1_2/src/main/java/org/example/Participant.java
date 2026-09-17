package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class with hand.
 */
public abstract class Participant {
    protected final List<Card> hand = new ArrayList<>();

    /**
     * Adds card to hand.
     * @param card given card
     */
    public void takeCard(Card card) {
        hand.add(card);
    }

    /**
     * Clears hand.
     */
    public void clearHand() {
        hand.clear();
    }

    /**
     * Returns hand.
     *
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Returns counted score.
     *
     */
    public int getScore() {
        int sum = 0;

        for (Card card : hand) {
            if (card.getRank() == Ranks.ACE) {
                card.setCurrentValue(11);
            }
            sum += card.getCurrentValue();
        }

        for (Card card : hand) {
            if (sum > 21 && card.getRank() == Ranks.ACE && card.getCurrentValue() == 11) {
                card.setCurrentValue(1);
                sum -= 10;
            }
        }

        return sum;
    }
}