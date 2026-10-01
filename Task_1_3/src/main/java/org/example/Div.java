package org.example;
import java.util.Map;
import java.util.Objects;

class Div extends Expression {
    private final Expression left, right;

    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "/" + right.toString() + ")";
    }

    @Override
    public Expression derivative(String variable) {
        Expression us = left.derivative(variable);
        Expression vs = right.derivative(variable);
        return new Div(new Sub(new Mul(us, right), new Mul(left, vs)), new Mul(right, right));
    }

    @Override
    protected int eval(Map<String, Integer> variables) {
        return left.eval(variables) / right.eval(variables);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        Div div = (Div) obj;
        return left.equals(div.left) && right.equals(div.right);
    }

    @Override
    public int hashCode() { return Objects.hash(left, right, "Div"); }

    @Override
    public Expression simplify() {
        Expression left = this.left.simplify();
        Expression right = this.right.simplify();
        if (left instanceof Number && ((Number) left).getValue() == 0) {
            return new Number(0);
        }
        if (right instanceof Number && ((Number) right).getValue() == 1) {
            return left;
        }
        if (left instanceof Number && right instanceof Number) {
            if (((Number) right).getValue() == 0) {
                throw new ArithmeticException("Division by zero");
            }
            return new Number(((Number) left).getValue() / ((Number) right).getValue());
        }
        return new Div(left, right);
    }
}
