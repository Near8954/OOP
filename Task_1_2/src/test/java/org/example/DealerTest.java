package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DealerTest {

    @Test
    void testGetHiddenHandStringWithTwoCards() {
        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Suits.HEARTS, Ranks.TEN));
        dealer.takeCard(new Card(Suits.SPADES, Ranks.ACE));

        String hiddenHand = dealer.getHiddenHandString();
        assertTrue(hiddenHand.contains("Десятка Червы"));
        assertTrue(hiddenHand.contains("<закрытая карта>"));
    }

    @Test
    void testGetHiddenHandStringWithOneCard() {
        Dealer dealer = new Dealer();
        dealer.takeCard(new Card(Suits.HEARTS, Ranks.TEN));
        assertEquals("[Десятка Червы (10)]", dealer.getHiddenHandString());
    }
}