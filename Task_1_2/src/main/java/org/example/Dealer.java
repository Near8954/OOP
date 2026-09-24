package org.example;

/**
 * Class for dealer.
 */
public class Dealer extends Participant {
    /**
     * Empty dealer's constructor.
     */
    public Dealer() {}
    // Малышка любит дилера, дилера —
    // Он стильный и смелый
    // (Он, стильный и смелый)
    /**
     * Returns hidden dealer's hand.
     *
     */
    public String getHiddenHandString() {
        if (hand.size() >= 2) {
            return "[" + hand.getFirst().toString() + ", <закрытая карта>]";
        }
        return hand.toString();
    }
}