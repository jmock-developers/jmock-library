/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.examples.calculator.expression;


import org.jmock.examples.calculator.CalculatorException;
import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.SimpleEnvironment;
import org.jmock.examples.calculator.expression.Division;


public class DivisionTest extends AbstractBinaryOperatorTest
{

    public void testDividesLeftSubexpressionByRightSubexpression() throws Exception {
        runOperatorTest();
    }

    public void testThrowsExceptionOnDivideByZero() throws Exception {
        Expression expression = makeExpression(1, 0);

        try {
            expression.evaluate(new SimpleEnvironment());
            fail("expected CalculatorException on divide by zero");
        }
        catch (CalculatorException ex) {
            assertTrue("error should contain 'divide by zero'",
                       ex.getMessage().indexOf("divide by zero") >= 0);
        }
    }

    protected double expectedValue( double left, double right ) {
        return left / right;
    }

    protected Expression makeExpression( Expression left, Expression right ) {
        return new Division(left, right);
    }
}
