package org.example;

import java.util.Map;
import java.util.Objects;

class Sub extends Expression {
    private final Expression left;
    private final Expression right;

    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "-" + right.toString() + ")";
    }

    @Override
    public Expression derivative(String variable) {
        return new  Sub(left.derivative(variable), right.derivative(variable));
    }

    @Override
    protected int eval(Map<String, Integer> variables) {
        return left.eval(variables) - right.eval(variables);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        Sub sub = (Sub) obj;
        return left.equals(sub.left) && right.equals(sub.right);
    }

    @Override
    public int hashCode() { return Objects.hash(left, right, "Sub"); }

    @Override
    public Expression simplify() {
        Expression left = this.left.simplify();
        Expression right = this.right.simplify();
        if (left.equals(right)) return new Number(0);
        if (right instanceof Number && ((Number) right).getValue() == 0) return left;
        if (left instanceof Number && right instanceof Number) {
            return new Number(((Number) left).getValue() - ((Number) right).getValue());
        }
        return new Sub(left, right);
    }
}