/* Copyright Dec 7, 2003 Nat Pryce, all rights reserved.
 */
package test.jmock.examples.calculator.expression;


import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.expression.Subtraction;

public class SubtractionTest extends AbstractBinaryOperatorTest {

    public void testSubtractsRightSubexpressionFromLeftSubexpression()
        throws Exception 
    {
        runOperatorTest();
    }

    protected double expectedValue(double left, double right) {
        return left - right;
    }

    protected Expression makeExpression(Expression left, Expression right) {
        return new Subtraction(left,right);
    }

}
