package org.example;

/** Main class to demonstrate expressions. */
public class Main {

    /**
     * Entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Expression e = new Add(new Number(3), new Mul(new Number(0), new Variable("x")));
        Expression simplified = e.simplify();
        System.out.print("Expression e: ");
        e.print();
        simplified.print();
    }
}