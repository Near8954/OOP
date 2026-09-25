package org.example;

import java.util.Scanner;

/**
 * Class which emulates game.
 */
public class Game {
    private final Dealer dealer;
    private final Player player;
    private final Scanner scanner;
    private Deck customDeck;
    private int playerScore = 0;
    private int dealerScore = 0;
    private int round = 1;
    static final int MAX_CARDS_SUM = 21;
    static final int DEALER_STOP_SCORE = 17;

    /**
     * Game initializer.
     */
    public Game() {
        this.dealer = new Dealer();
        this.player = new Player();
        this.scanner = new Scanner(System.in);
    }

    public Game(Scanner scanner, Deck deck) {
        this.dealer = new Dealer();
        this.player = new Player();
        this.scanner = scanner;
        this.customDeck = deck;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public int getDealerScore() {
        return dealerScore;
    }

    /**
     * Starts game emulation.
     */
    public void startGame() {
        System.out.println("Добро пожаловать в Блэкджек!");

        while (true) {
            playRound();

            System.out.println("\nХотите сыграть еще один раунд? (1 - Да, 0 - Нет)");
            if (scanner.hasNextLine()) {
                String choice = scanner.nextLine();
                if ("0".equals(choice)) {
                    System.out.println("Игра завершена. Итоговый счет -> Вы: "
                            + playerScore + " | Дилер: " + dealerScore);
                    break;
                }
            } else {
                break;
            }
        }
        if (this.customDeck == null) {
            scanner.close();
        }
    }

    /**
     * Starts round.
     */
    private void playRound() {
        System.out.println("\nРаунд " + round);
        player.clearHand();
        dealer.clearHand();

        Deck currentDeck;
        if (this.customDeck != null) {
            currentDeck = this.customDeck;
        } else {
            currentDeck = new Deck();
            currentDeck.shuffleDeck();
        }

        player.takeCard(currentDeck.getCard());
        dealer.takeCard(currentDeck.getCard());
        player.takeCard(currentDeck.getCard());
        dealer.takeCard(currentDeck.getCard());

        System.out.println("Дилер раздал карты");
        printTable(true);

        if (player.getScore() == MAX_CARDS_SUM && dealer.getScore() == MAX_CARDS_SUM) {
            System.out.println("Ничья!");
            round++;
            return;
        } else if (dealer.getScore() == MAX_CARDS_SUM) {
            System.out.println("Блэкджек! Дилер выиграл раунд!");
            dealerScore++;
            round++;
            return;
        } else if (player.getScore() == MAX_CARDS_SUM) {
            System.out.println("Блэкджек! Вы выиграли раунд!");
            playerScore++;
            round++;
            return;
        }

        System.out.println("\nВаш ход");
        System.out.println("-------");
        boolean playerBust = false;

        while (player.getScore() < MAX_CARDS_SUM) {
            System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться...");
            String input = scanner.nextLine();

            if ("0".equals(input)) {
                break;
            } else if ("1".equals(input)) {
                Card newCard = currentDeck.getCard();
                player.takeCard(newCard);
                System.out.println("Вы открыли карту " + newCard.toString());
                printTable(true);
            }
        }

        if (player.getScore() > MAX_CARDS_SUM) {
            playerBust = true;
        }

        if (!playerBust) {
            System.out.println("\nХод дилера");
            System.out.println("-------");
            System.out.println("Дилер открывает закрытую карту "
                    + dealer.getHand().get(1).toString());
            printTable(false);

            while (dealer.getScore() < DEALER_STOP_SCORE) {
                Card newCard = currentDeck.getCard();
                dealer.takeCard(newCard);
                System.out.println("\nДилер открывает карту " + newCard.toString());
                printTable(false);
            }
        }

        determineWinner(playerBust);
        round++;
    }

    /**
     * Prints table with players and dealers cards to console.
     * @param hideDealerCard parameter which hides dealers cards.
     */
    private void printTable(boolean hideDealerCard) {
        System.out.println("    Ваши карты: " + player.getHand().toString()
                + " => " + player.getScore());
        if (hideDealerCard) {
            System.out.println("    Карты дилера: "
                    + dealer.getHiddenHandString());
        } else {
            System.out.println("    Карты дилера: " + dealer.getHand().toString()
                    + " => " + dealer.getScore());
        }
    }

    /**
     * Determines winner in current round.
     * @param playerBust true if player's score exceeded 21 else false
     */
    private void determineWinner(boolean playerBust) {
        int pScore = player.getScore();
        int dScore = dealer.getScore();

        if (playerBust) {
            dealerScore++;
            System.out.println("\nПеребор! Вы проиграли раунд. Счет " + playerScore
                    + ":" + dealerScore + " в пользу дилера.");
        } else if (dScore > MAX_CARDS_SUM || pScore > dScore) {
            playerScore++;
            System.out.println("\nВы выиграли раунд! Счет " + playerScore
                    + ":" + dealerScore + " в вашу пользу.");
        } else if (dScore > pScore) {
            dealerScore++;
            System.out.println("\nВы проиграли раунд. Счет " + playerScore
                    + ":" + dealerScore + " в пользу дилера.");
        } else {
            System.out.println("\nНичья! Счет " + playerScore + ":" + dealerScore);
        }
    }
}