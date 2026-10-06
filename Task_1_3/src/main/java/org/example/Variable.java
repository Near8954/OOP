package org.example;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a variable in an expression.
 */
class Variable extends Expression {

    private final String name;

    /**
     * Creates a variable.
     *
     * @param name variable name
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Gets the variable name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the string representation.
     *
     * @return string format
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Calculates the derivative.
     *
     * @param variable variable to differentiate by
     * @return 1 if names match, else 0
     */
    @Override
    public Expression derivative(String variable) {
        if (this.name.equals(variable)) {
            return new Number(1);
        }
        return new Number(0);
    }

    /**
     * Evaluates the variable.
     *
     * @param variables map of variable values
     * @return variable value
     * @throws ArithmeticException if not found
     */
    @Override
    protected int eval(Map<String, Integer> variables) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }
        throw new ArithmeticException("Variable " + name + " not found");
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
        Variable variable = (Variable) obj;
        return Objects.equals(name, variable.name);
    }

    /**
     * Returns hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Simplifies the variable (returns a copy).
     *
     * @return simplified expression
     */
    @Override
    public Expression simplify() {
        return new Variable(name);
    }
}