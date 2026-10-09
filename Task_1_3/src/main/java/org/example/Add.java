package org.example;

import java.util.Map;
import java.util.Objects;

/**
 * Represents the addition of two expressions.
 */
class Add extends Expression {

    private final Expression left;
    private final Expression right;

    /**
     * Creates an addition operation.
     *
     * @param left  left expression
     * @param right right expression
     */
    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the string representation.
     *
     * @return string format "(left+right)"
     */
    @Override
    public String toString() {
        return "(" + left.toString() + "+" + right.toString() + ")";
    }

    /**
     * Calculates the derivative.
     *
     * @param variable variable to differentiate by
     * @return derivative expression
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(left.derivative(variable), right.derivative(variable));
    }

    /**
     * Evaluates the addition.
     *
     * @param variables map of variable values
     * @return sum
     */
    @Override
    protected int eval(Map<String, Integer> variables) {
        return left.eval(variables) + right.eval(variables);
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
        Add add = (Add) obj;
        return left.equals(add.left) && right.equals(add.right);
    }

    /**
     * Returns hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(left, right, "Add");
    }

    /**
     * Simplifies the addition expression.
     *
     * @return simplified expression
     */
    @Override
    public Expression simplify() {
        Expression left = this.left.simplify();
        Expression right = this.right.simplify();
        if (left instanceof Number && ((Number) left).getValue() == 0) {
            return right;
        } else if (right instanceof Number && ((Number) right).getValue() == 0) {
            return left;
        } else if (left instanceof Number && right instanceof Number) {
            return new Number(((Number) left).getValue() + ((Number) right).getValue());
        }
        return new Add(left, right);
    }
}