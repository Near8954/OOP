package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Variable class.
 */
class VariableTest {

    /**
     * Tests basic variable properties and derivatives.
     */
    @Test
    void testVariable() {
        Variable var = new Variable("x");
        assertEquals("x", var.toString());
        assertEquals("x", var.getName());
        assertEquals(new Number(1), var.derivative("x"));
        assertEquals(new Number(0), var.derivative("y"));
        assertEquals(var, var.simplify());
    }

    /**
     * Tests evaluation with missing and present assignments.
     */
    @Test
    void testEval() {
        Variable var = new Variable("x");
        assertEquals(10, var.eval("x=10; y=5"));
        assertThrows(ArithmeticException.class, () -> var.eval("y=5"));
    }

    /**
     * Tests equals and hashCode methods.
     */
    @Test
    void testEqualsAndHashCode() {
        Variable v1 = new Variable("x");
        Variable v2 = new Variable("x");
        Variable v3 = new Variable("y");

        assertTrue(v1.equals(v1));
        assertTrue(v1.equals(v2));
        assertFalse(v1.equals(v3));
        assertFalse(v1.equals(null));
        assertFalse(v1.equals(new Number(1)));
        assertEquals(v1.hashCode(), v2.hashCode());
    }
}