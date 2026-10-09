package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Div class.
 */
class DivTest {

    /**
     * Tests the toString method.
     */
    @Test
    void testToString() {
        assertEquals("(4/2)", new Div(new Number(4), new Number(2)).toString());
    }

    /**
     * Tests evaluation and division by zero exceptions.
     */
    @Test
    void testEval() {
        assertEquals(2, new Div(new Number(4), new Number(2)).eval(""));
        assertThrows(ArithmeticException.class,
            () -> new Div(new Number(1), new Number(0)).eval(""));
    }

    /**
     * Tests the derivative calculation.
     */
    @Test
    void testDerivative() {
        Expression div = new Div(new Variable("x"), new Variable("y"));
        Expression diff = div.derivative("x");
        assertNotNull(diff);
    }

    /**
     * Tests all branches of the simplify method.
     */
    @Test
    void testSimplify() {
        assertEquals(new Number(0), new Div(new Number(0), new Variable("x")).simplify());
        assertEquals(new Variable("x"), new Div(new Variable("x"), new Number(1)).simplify());
        assertEquals(new Number(2), new Div(new Number(4), new Number(2)).simplify());
        assertThrows(ArithmeticException.class,
            () -> new Div(new Number(4), new Number(0)).simplify());

        Expression unsimplifiable = new Div(new Variable("x"), new Variable("y"));
        assertEquals(unsimplifiable, unsimplifiable.simplify());
    }

    /**
     * Tests equals and hashCode methods.
     */
    @Test
    void testEqualsAndHashCode() {
        Div d1 = new Div(new Variable("x"), new Number(2));
        Div d2 = new Div(new Variable("x"), new Number(2));
        Div d3 = new Div(new Number(2), new Variable("x"));

        assertTrue(d1.equals(d1));
        assertTrue(d1.equals(d2));
        assertFalse(d1.equals(d3));
        assertFalse(d1.equals(null));
        assertFalse(d1.equals(new Number(1)));
        assertEquals(d1.hashCode(), d2.hashCode());
    }
}