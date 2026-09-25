package org.example;

import org.junit.jupiter.api.Test;
import java.util.Scanner;
import java.util.Stack;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Deck createTestDeck(Card... cards) {
        Stack<Card> stack = new Stack<>();
        for (int i = cards.length - 1; i >= 0; i--) {
            stack.push(cards[i]);
        }
        return new Deck(stack);
    }

    @Test
    void testPlayerBlackjackFromStart() {
        Deck deck = createTestDeck(
                new Card(Suits.HEARTS, Ranks.ACE),
                new Card(Suits.CLUBS, Ranks.TWO),
                new Card(Suits.SPADES, Ranks.KING),
                new Card(Suits.DIAMONDS, Ranks.THREE)
        );

        Scanner scanner = new Scanner("0\n");
        Game game = new Game(scanner, deck);

        game.startGame();

        assertEquals(1, game.getPlayerScore());
        assertEquals(0, game.getDealerScore());
    }

    @Test
    void testMutualBlackjackFromStart() {
        Deck deck = createTestDeck(
                new Card(Suits.HEARTS, Ranks.ACE),
                new Card(Suits.CLUBS, Ranks.ACE),
                new Card(Suits.SPADES, Ranks.KING),
                new Card(Suits.DIAMONDS, Ranks.KING)
        );

        Scanner scanner = new Scanner("0\n");
        Game game = new Game(scanner, deck);

        game.startGame();

        assertEquals(0, game.getPlayerScore());
        assertEquals(0, game.getDealerScore());
    }

    @Test
    void testDealerBlackjackFromStart() {
        Deck deck = createTestDeck(
                new Card(Suits.HEARTS, Ranks.TEN),
                new Card(Suits.CLUBS, Ranks.ACE),
                new Card(Suits.SPADES, Ranks.FIVE),
                new Card(Suits.DIAMONDS, Ranks.KING)
        );

        Scanner scanner = new Scanner("0\n");
        Game game = new Game(scanner, deck);

        game.startGame();

        assertEquals(0, game.getPlayerScore());
        assertEquals(1, game.getDealerScore());
    }

    @Test
    void testPlayerBust() {
        Deck deck = createTestDeck(
                new Card(Suits.HEARTS, Ranks.TEN),
                new Card(Suits.CLUBS, Ranks.TEN),
                new Card(Suits.SPADES, Ranks.SEVEN),
                new Card(Suits.DIAMONDS, Ranks.SIX),
                new Card(Suits.HEARTS, Ranks.FIVE)
        );

        Scanner scanner = new Scanner("1\n0\n");
        Game game = new Game(scanner, deck);

        game.startGame();

        assertEquals(0, game.getPlayerScore());
        assertEquals(1, game.getDealerScore());
    }

    @Test
    void testPlayerStandsAndDealerBusts() {
        Deck deck = createTestDeck(
                new Card(Suits.HEARTS, Ranks.TEN),
                new Card(Suits.CLUBS, Ranks.TEN),
                new Card(Suits.SPADES, Ranks.NINE),
                new Card(Suits.DIAMONDS, Ranks.SIX),
                new Card(Suits.HEARTS, Ranks.SIX)
        );

        Scanner scanner = new Scanner("0\n0\n");
        Game game = new Game(scanner, deck);

        game.startGame();

        assertEquals(1, game.getPlayerScore());
        assertEquals(0, game.getDealerScore());
    }

    @Test
    void testDealerWinsByPoints() {
        Deck deck = createTestDeck(
                new Card(Suits.HEARTS, Ranks.TEN),
                new Card(Suits.CLUBS, Ranks.TEN),
                new Card(Suits.SPADES, Ranks.EIGHT),
                new Card(Suits.DIAMONDS, Ranks.NINE)
        );

        Scanner scanner = new Scanner("0\n0\n");
        Game game = new Game(scanner, deck);

        game.startGame();

        assertEquals(0, game.getPlayerScore());
        assertEquals(1, game.getDealerScore());
    }

    @Test
    void testTieByPoints() {
        Deck deck = createTestDeck(
                new Card(Suits.HEARTS, Ranks.TEN),
                new Card(Suits.CLUBS, Ranks.TEN),
                new Card(Suits.SPADES, Ranks.EIGHT),
                new Card(Suits.DIAMONDS, Ranks.EIGHT)
        );

        Scanner scanner = new Scanner("0\n0\n");
        Game game = new Game(scanner, deck);

        game.startGame();

        assertEquals(0, game.getPlayerScore());
        assertEquals(0, game.getDealerScore());
    }
}