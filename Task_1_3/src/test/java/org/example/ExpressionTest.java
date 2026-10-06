package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Expression base class.
 */
class ExpressionTest {

    /**
     * Tests the parser with various formatting, multidigit numbers, and variables.
     */
    @Test
    void testParseAdvanced() {
        Expression expr1 = Expression.parseAdvanced(" 2 * ( xVar + 13 ) ");
        Expression expected1 = new Mul(new Number(2),
            new Add(new Variable("xVar"), new Number(13)));
        assertEquals(expected1, expr1);

        Expression expr2 = Expression.parseAdvanced("10/2-1");
        Expression expected2 = new Sub(new Div(new Number(10), new Number(2)), new Number(1));
        assertEquals(expected2, expr2);
    }

    /**
     * Tests variable assignment parsing including empty, null, and invalid inputs.
     */
    @Test
    void testEvalWithAssignments() {
        Expression expr = new Add(new Variable("x"), new Number(5));
        assertEquals(15, expr.eval(" x = 10 ; y = 20 "));
        assertThrows(IllegalArgumentException.class, () -> expr.eval(""));
        assertThrows(IllegalArgumentException.class, () -> expr.eval((String) null));
        assertThrows(IllegalArgumentException.class, () -> expr.eval("invalid_assignment_format"));
    }

    /**
     * Tests the print method to ensure no exceptions are thrown.
     */
    @Test
    void testPrint() {
        Expression expr = new Number(42);
        assertDoesNotThrow(expr::print);
    }
}