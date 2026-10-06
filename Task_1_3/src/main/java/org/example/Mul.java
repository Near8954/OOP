package org.example;

import java.util.Map;
import java.util.Objects;

/** Represents the multiplication of two expressions. */
class Mul extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Creates a multiplication operation.
     *
     * @param left  left expression
     * @param right right expression
     */
    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the string representation.
     *
     * @return string format "(left*right)"
     */
    @Override
    public String toString() {
        return "(" + left.toString() + "*" + right.toString() + ")";
    }

    /**
     * Calculates the derivative.
     *
     * @param variable variable to differentiate by
     * @return derivative expression
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(new Mul(left.derivative(variable), right),
                new Mul(right.derivative(variable), left));
    }

    /**
     * Evaluates the multiplication.
     *
     * @param variables map of variable values
     * @return product
     */
    @Override
    protected int eval(Map<String, Integer> variables) {
        return left.eval(variables) * right.eval(variables);
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
        Mul mul = (Mul) obj;
        return left.equals(mul.left) && right.equals(mul.right);
    }

    /**
     * Returns hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode() { return Objects.hash(left, right, "Mul"); }

    /**
     * Simplifies the multiplication expression.
     *
     * @return simplified expression
     */
    @Override
    public Expression simplify() {
        Expression left = this.left.simplify();
        Expression right = this.right.simplify();
        if (left instanceof Number && ((Number) left).getValue() == 0) {
            return new Number(0);
        }
        if (right instanceof Number && ((Number) right).getValue() == 0) {
            return new Number(0);
        }
        if (left instanceof Number && ((Number) left).getValue() == 1) {
            return right;
        }
        if (right instanceof Number && ((Number) right).getValue() == 1) {
            return left;
        }
        if (left instanceof Number && right instanceof Number) {
            return new Number(((Number) left).getValue() *
                    ((Number) right).getValue());
        }
        return new Mul(left, right);
    }
}