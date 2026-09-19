package org.example;

/**
 * Enum for card's ranks.
 */
public enum Ranks {
    TWO("Двойка", 2),
    THREE("Тройка", 3),
    FOUR("Четверка", 4),
    FIVE("Пятерка", 5),
    SIX("Шестерка", 6),
    SEVEN("Семерка", 7),
    EIGHT("Восьмерка", 8),
    NINE("Девятка", 9),
    TEN("Десятка", 10),
    JACK("Валет", 10),
    QUEEN("Дама", 10),
    KING("Король", 10),
    ACE("Туз", 11);

    private final String name;
    private final int value;

    /**
     * Ranks constructor.
     * @param name given name
     * @param value given value
     */
    Ranks(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns rank's name.
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Returns rank's value.
     *
     */
    public int getValue() {
        return value;
    }
}