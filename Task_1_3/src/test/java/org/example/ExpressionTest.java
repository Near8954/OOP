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

        Expression expr3 = Expression.parseAdvanced("2+3*4");
        Expression expected3 = new Add(new Number(2), new Mul(new Number(3), new Number(4)));
        assertEquals(expected3, expr3);

        Expression expr4 = Expression.parseAdvanced("(2+3)*4");
        Expression expected4 = new Mul(new Add(new Number(2), new Number(3)), new Number(4));
        assertEquals(expected4, expr4);

        Expression expr5 = Expression.parseAdvanced("x * 2 + (y - 3) / z");
        Expression expected5 = new Add(
            new Mul(new Variable("x"), new Number(2)),
            new Div(new Sub(new Variable("y"), new Number(3)), new Variable("z"))
        );
        assertEquals(expected5, expr5);

        Expression expr6 = Expression.parseAdvanced("((a + b) * c) / (d - e)");
        Expression expected6 = new Div(
            new Mul(new Add(new Variable("a"), new Variable("b")), new Variable("c")),
            new Sub(new Variable("d"), new Variable("e"))
        );
        assertEquals(expected6, expr6);
    }

    /**
     * Tests variable assignment parsing including empty, null, and invalid inputs.
     */
    @Test
    void testEvalWithAssignments() {
        Expression expr = new Add(new Variable("x"), new Number(5));
        assertEquals(15, expr.eval(" x = 10 ; y = 20 "));

        Expression complexExpr = Expression.parseAdvanced("2 * x + (y - 3) / z");
        assertEquals(13, complexExpr.eval("x=5; y=15; z=4")); // 2 * 5 + (15 - 3) / 4 = 10 + 3 = 13

        assertThrows(IllegalArgumentException.class, () -> expr.eval(""));
        assertThrows(IllegalArgumentException.class, () -> expr.eval((String) null));
        assertThrows(IllegalArgumentException.class, () -> expr.eval("invalid_assignment_format"));
    }

    /**
     * Tests the print method to ensure no exceptions are thrown.
     */
    @Test
    void testPrint() {
        Expression expr = new Add(new Number(42), new Mul(new Variable("x"), new Number(10)));
        assertDoesNotThrow(expr::print);
    }
}