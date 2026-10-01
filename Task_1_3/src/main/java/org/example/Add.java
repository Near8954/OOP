package org.example;
import java.util.Map;
import java.util.Objects;

class Add extends Expression {
    private final Expression left, right;

    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "+" + right.toString() + ")";
    }

    @Override
    public Expression derivative(String variable) {
        return new Add(left.derivative(variable), right.derivative(variable));
    }

    @Override
    protected int eval(Map<String, Integer> variables) {
        return left.eval(variables) + right.eval(variables);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null || getClass() != obj.getClass()) {return false;}
        Add add = (Add) obj;
        return left.equals(add.left) && right.equals(add.right);
    }

    @Override
    public int hashCode() { return Objects.hash(left, right, "Add"); }

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