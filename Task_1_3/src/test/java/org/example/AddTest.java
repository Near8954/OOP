package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Add class.
 */
class AddTest {

    /**
     * Tests the toString method.
     */
    @Test
    void testToString() {
        assertEquals("(1+2)", new Add(new Number(1), new Number(2)).toString());
    }

    /**
     * Tests the evaluation of addition.
     */
    @Test
    void testEval() {
        assertEquals(5, new Add(new Number(2), new Number(3)).eval(""));
    }

    /**
     * Tests the derivative calculation.
     */
    @Test
    void testDerivative() {
        Expression add = new Add(new Variable("x"), new Number(3));
        Expression diff = add.derivative("x");
        assertEquals(new Add(new Number(1), new Number(0)), diff);
    }

    /**
     * Tests all branches of the simplify method.
     */
    @Test
    void testSimplify() {
        assertEquals(new Variable("x"), new Add(new Number(0), new Variable("x")).simplify());
        assertEquals(new Variable("x"), new Add(new Variable("x"), new Number(0)).simplify());
        assertEquals(new Number(5), new Add(new Number(2), new Number(3)).simplify());

        Expression unsimplifiable = new Add(new Variable("x"), new Variable("y"));
        assertEquals(unsimplifiable, unsimplifiable.simplify());
    }

    /**
     * Tests equals and hashCode methods.
     */
    @Test
    void testEqualsAndHashCode() {
        Add a1 = new Add(new Variable("x"), new Number(1));
        Add a2 = new Add(new Variable("x"), new Number(1));
        Add a3 = new Add(new Number(1), new Variable("x"));

        assertTrue(a1.equals(a1));
        assertTrue(a1.equals(a2));
        assertFalse(a1.equals(a3));
        assertFalse(a1.equals(null));
        assertFalse(a1.equals(new Number(1)));
        assertEquals(a1.hashCode(), a2.hashCode());
    }
}