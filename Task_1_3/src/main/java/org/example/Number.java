package org.example;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a constant number in an expression.
 */
class Number extends Expression {

    private final int value;

    /**
     * Creates a number constant.
     *
     * @param value the integer value
     */
    public Number(int value) {
        this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return integer value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the string representation.
     *
     * @return string format
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Calculates the derivative (always 0).
     *
     * @param variable variable
     * @return derivative expression (0)
     */
    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }

    /**
     * Evaluates the number.
     *
     * @param variables ignored
     * @return the value
     */
    @Override
    protected int eval(Map<String, Integer> variables) {
        return value;
    }

    /**
     * Checks equality.
     *
     * @param obj object to compare
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Number number = (Number) obj;
        return value == number.value;
    }

    /**
     * Returns hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Simplifies the number (returns a copy).
     *
     * @return simplified expression
     */
    @Override
    public Expression simplify() {
        return new Number(value);
    }
}