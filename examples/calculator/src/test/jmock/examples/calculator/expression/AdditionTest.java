/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.examples.calculator.expression;


import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.expression.Addition;


public class AdditionTest extends AbstractBinaryOperatorTest
{

    public void testAddsLeftAndRightSubexpressions() throws Exception {
        runOperatorTest();
    }

    protected double expectedValue( double left, double right ) {
        return left + right;
    }

    protected Expression makeExpression( Expression left, Expression right ) {
        return new Addition(left, right);
    }
}
