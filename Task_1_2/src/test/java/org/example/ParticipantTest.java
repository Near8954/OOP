package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    @Test
    void testTakeCardAndClearHand() {
        Participant player = new Player();
        player.takeCard(new Card(Suits.CLUBS, Ranks.TEN));
        assertEquals(1, player.getHand().size());

        player.clearHand();
        assertEquals(0, player.getHand().size());
    }

    @Test
    void testGetScoreWithoutAce() {
        Participant player = new Player();
        player.takeCard(new Card(Suits.CLUBS, Ranks.TEN));
        player.takeCard(new Card(Suits.HEARTS, Ranks.SEVEN));
        assertEquals(17, player.getScore());
    }

    @Test
    void testGetScoreWithAceNoBust() {
        Participant player = new Player();
        player.takeCard(new Card(Suits.CLUBS, Ranks.ACE));
        player.takeCard(new Card(Suits.HEARTS, Ranks.NINE));
        assertEquals(20, player.getScore());
    }

    @Test
    void testGetScoreWithAceBust() {
        Participant player = new Player();
        player.takeCard(new Card(Suits.CLUBS, Ranks.TEN));
        player.takeCard(new Card(Suits.HEARTS, Ranks.NINE));
        player.takeCard(new Card(Suits.SPADES, Ranks.ACE));
        assertEquals(20, player.getScore());
    }
}