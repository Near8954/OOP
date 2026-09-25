package org.example;

/**
 * Class for card.
 */
public class Card {
    private final Suits suit;
    private final Ranks rank;
    private int currentValue;

    /**
     * Creates card object.
     * @param suit card's suit
     * @param rank card's rank
     */
    public Card(Suits suit, Ranks rank) {
        this.suit = suit;
        this.rank = rank;
        this.currentValue = rank.getValue();
    }

    /**
     * Returns card's suit.
     *
     */
    public Suits getSuit() {
        return this.suit;
    }

    /**
     * Returns card's rank.
     *
     */
    public Ranks getRank() {
        return this.rank;
    }

    /**
     * Returns card's current value.
     *
     */
    public int getCurrentValue() {
        return this.currentValue;
    }

    /**
     * Sets card's current value.
     * @param currentValue new card's value
     */
    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * Makes string from card's parameters.
     *
     */
    @Override
    public String toString() {
        return rank.getName() + " " + suit.getName() + " (" + currentValue + ")";
    }
}