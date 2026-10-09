package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Number class.
 */
class NumberTest {

    /**
     * Tests basic number properties, evaluations, and derivatives.
     */
    @Test
    void testNumber() {
        Number num = new Number(5);
        assertEquals("5", num.toString());
        assertEquals(5, num.getValue());
        assertEquals(5, num.eval(""));
        assertEquals(new Number(0), num.derivative("x"));
        assertEquals(num, num.simplify());
    }

    /**
     * Tests equals and hashCode methods.
     */
    @Test
    void testEqualsAndHashCode() {
        Number n1 = new Number(5);
        Number n2 = new Number(5);
        Number n3 = new Number(10);

        assertTrue(n1.equals(n1));
        assertTrue(n1.equals(n2));
        assertFalse(n1.equals(n3));
        assertFalse(n1.equals(null));
        assertFalse(n1.equals(new Variable("x")));
        assertEquals(n1.hashCode(), n2.hashCode());
    }
}