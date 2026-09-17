package org.example;

/**
 * Enum for card's suits.
 */
public enum Suits {
    HEARTS("Червы"),
    DIAMONDS("Бубны"),
    CLUBS("Трефы"),
    SPADES("Пики");

    private final String name;

    /**
     * Sets suit name.
     * @param name given name
     */
    Suits(String name) {
        this.name = name;
    }

    /**
     * Returns name.
     *
     */
    public String getName() {
        return name;
    }
}