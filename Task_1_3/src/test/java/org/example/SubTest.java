package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Sub class.
 */
class SubTest {

    /**
     * Tests the toString method.
     */
    @Test
    void testToString() {
        assertEquals("(5-3)", new Sub(new Number(5), new Number(3)).toString());
    }

    /**
     * Tests the evaluation of subtraction.
     */
    @Test
    void testEval() {
        assertEquals(2, new Sub(new Number(5), new Number(3)).eval(""));
    }

    /**
     * Tests the derivative calculation.
     */
    @Test
    void testDerivative() {
        Expression sub = new Sub(new Variable("x"), new Number(3));
        Expression diff = sub.derivative("x");
        assertEquals(new Sub(new Number(1), new Number(0)), diff);
    }

    /**
     * Tests all branches of the simplify method.
     */
    @Test
    void testSimplify() {
        assertEquals(new Number(0), new Sub(new Variable("x"), new Variable("x")).simplify());
        assertEquals(new Variable("x"), new Sub(new Variable("x"), new Number(0)).simplify());
        assertEquals(new Number(2), new Sub(new Number(5), new Number(3)).simplify());

        Expression unsimplifiable = new Sub(new Variable("x"), new Variable("y"));
        assertEquals(unsimplifiable, unsimplifiable.simplify());
    }

    /**
     * Tests equals and hashCode methods.
     */
    @Test
    void testEqualsAndHashCode() {
        Sub s1 = new Sub(new Variable("x"), new Number(2));
        Sub s2 = new Sub(new Variable("x"), new Number(2));
        Sub s3 = new Sub(new Number(2), new Variable("x"));

        assertTrue(s1.equals(s1));
        assertTrue(s1.equals(s2));
        assertFalse(s1.equals(s3));
        assertFalse(s1.equals(null));
        assertFalse(s1.equals(new Number(1)));
        assertEquals(s1.hashCode(), s2.hashCode());
    }
}