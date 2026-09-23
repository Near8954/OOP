package org.example;

import org.junit.jupiter.api.Test;
import java.util.EmptyStackException;
import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void testDeckInitialization() {
        Deck deck = new Deck();
        for (int i = 0; i < 52; i++) {
            assertNotNull(deck.getCard());
        }
        assertThrows(EmptyStackException.class, deck::getCard);
    }

    @Test
    void testShuffleDeck() {
        Deck deck = new Deck();
        assertDoesNotThrow(deck::shuffleDeck);
        assertNotNull(deck.getCard());
    }
}