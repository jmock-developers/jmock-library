/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.examples.calculator.expression;

import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.expression.Power;


public class PowerTest extends AbstractBinaryOperatorTest
{

    public void testRaisesLeftSubexpressionToThePowerOfRightSubexpressions() throws Exception {
        runOperatorTest();
    }

    protected double expectedValue( double left, double right ) {
        return Math.pow(left, right);
    }

    protected Expression makeExpression( Expression left, Expression right ) {
        return new Power(left, right);
    }
}
