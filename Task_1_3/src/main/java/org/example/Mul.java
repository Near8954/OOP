package org.example;

import java.util.Map;
import java.util.Objects;


class Mul extends Expression {
    private final Expression left;
    private final Expression right;

    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "*" + right.toString() + ")";
    }

    @Override
    public Expression derivative(String variable) {
        return new Add(new Mul(left.derivative(variable), right),
                       new Mul(right.derivative(variable), left));
    }

    @Override
    protected int eval(Map<String, Integer> variables) {
        return left.eval(variables) * right.eval(variables);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null || getClass() != obj.getClass()) { return false; }
        Mul mul = (Mul) obj;
        return left.equals(mul.left) && right.equals(mul.right);
    }

    @Override
    public int hashCode() { return Objects.hash(left, right, "Mul"); }

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