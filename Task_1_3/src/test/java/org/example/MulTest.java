package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Mul class.
 */
class MulTest {

    /**
     * Tests the toString method.
     */
    @Test
    void testToString() {
        assertEquals("(2*3)", new Mul(new Number(2), new Number(3)).toString());
    }

    /**
     * Tests the evaluation of multiplication.
     */
    @Test
    void testEval() {
        assertEquals(6, new Mul(new Number(2), new Number(3)).eval(""));
    }

    /**
     * Tests the derivative calculation.
     */
    @Test
    void testDerivative() {
        Expression mul = new Mul(new Variable("x"), new Number(3));
        Expression diff = mul.derivative("x");
        assertNotNull(diff);
    }

    /**
     * Tests all branches of the simplify method.
     */
    @Test
    void testSimplify() {
        assertEquals(new Number(0), new Mul(new Number(0), new Variable("x")).simplify());
        assertEquals(new Number(0), new Mul(new Variable("x"), new Number(0)).simplify());
        assertEquals(new Variable("x"), new Mul(new Number(1), new Variable("x")).simplify());
        assertEquals(new Variable("x"), new Mul(new Variable("x"), new Number(1)).simplify());
        assertEquals(new Number(6), new Mul(new Number(2), new Number(3)).simplify());

        Expression unsimplifiable = new Mul(new Variable("x"), new Variable("y"));
        assertEquals(unsimplifiable, unsimplifiable.simplify());
    }

    /**
     * Tests equals and hashCode methods.
     */
    @Test
    void testEqualsAndHashCode() {
        Mul m1 = new Mul(new Variable("x"), new Number(2));
        Mul m2 = new Mul(new Variable("x"), new Number(2));
        Mul m3 = new Mul(new Number(2), new Variable("x"));

        assertTrue(m1.equals(m1));
        assertTrue(m1.equals(m2));
        assertFalse(m1.equals(m3));
        assertFalse(m1.equals(null));
        assertFalse(m1.equals(new Number(1)));
        assertEquals(m1.hashCode(), m2.hashCode());
    }
}