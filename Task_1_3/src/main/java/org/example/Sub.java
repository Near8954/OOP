package org.example;

import java.util.Map;
import java.util.Objects;

/**
 * Represents the subtraction of two expressions.
 */
class Sub extends Expression {

    private final Expression left;
    private final Expression right;

    /**
     * Creates a subtraction operation.
     *
     * @param left  minuend
     * @param right subtrahend
     */
    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the string representation.
     *
     * @return string format "(left-right)"
     */
    @Override
    public String toString() {
        return "(" + left.toString() + "-" + right.toString() + ")";
    }

    /**
     * Calculates the derivative.
     *
     * @param variable variable to differentiate by
     * @return derivative expression
     */
    @Override
    public Expression derivative(String variable) {
        return new Sub(left.derivative(variable), right.derivative(variable));
    }

    /**
     * Evaluates the subtraction.
     *
     * @param variables map of variable values
     * @return difference
     */
    @Override
    protected int eval(Map<String, Integer> variables) {
        return left.eval(variables) - right.eval(variables);
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
        Sub sub = (Sub) obj;
        return left.equals(sub.left) && right.equals(sub.right);
    }

    /**
     * Returns hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(left, right, "Sub");
    }

    /**
     * Simplifies the subtraction expression.
     *
     * @return simplified expression
     */
    @Override
    public Expression simplify() {
        Expression left = this.left.simplify();
        Expression right = this.right.simplify();
        if (left.equals(right)) {
            return new Number(0);
        }
        if (right instanceof Number && ((Number) right).getValue() == 0) {
            return left;
        }
        if (left instanceof Number && right instanceof Number) {
            return new Number(((Number) left).getValue() - ((Number) right).getValue());
        }
        return new Sub(left, right);
    }
}