package org.example;


public class Main {
    public static void main(String[] args) {
        Expression e = new Add(new Number(3), new Mul(new Number(0), new Variable("x")));
        Expression simplified = e.simplify();
        System.out.print("Выражение e: ");
        e.print();
        simplified.print();
    }
}