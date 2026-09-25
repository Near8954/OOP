package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void testCardCreationAndGetters() {
        Card card = new Card(Suits.SPADES, Ranks.ACE);
        assertEquals(Suits.SPADES, card.getSuit());
        assertEquals(Ranks.ACE, card.getRank());
        assertEquals(11, card.getCurrentValue());
    }

    @Test
    void testSetCurrentValue() {
        Card card = new Card(Suits.HEARTS, Ranks.ACE);
        card.setCurrentValue(1);
        assertEquals(1, card.getCurrentValue());
    }

    @Test
    void testToString() {
        Card card = new Card(Suits.DIAMONDS, Ranks.JACK);
        assertEquals("Валет Бубны (10)", card.toString());
    }
}