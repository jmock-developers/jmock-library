/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.calculator.acceptance;

import junit.framework.TestCase;
import org.jmock.examples.calculator.Calculator;


public class CalculatorAcceptanceTest extends TestCase
{
    private Calculator calculator;

    public void setUp() {
        calculator = new Calculator();
    }

    public void testLiteral() throws Exception {
        assertEquals("should be 9", 9, calculator.calculate("9"), 0);
    }

    public void testSimpleCalculation() throws Exception {
        assertEquals("should be 7", 7, calculator.calculate("1 + 2 * 3"), 0);
    }

    public void testCalculationWithVariables() throws Exception {
        calculator.setVariable("x", "1 + 2");
        calculator.setVariable("y", "x * 3");

        assertEquals("should be 9", 9, calculator.calculate("y"), 0);
    }

    public void testCalculationWithParentheses() throws Exception {
        assertEquals("should be 9", 9, calculator.calculate("(1+2)*3"), 0);
    }
}
