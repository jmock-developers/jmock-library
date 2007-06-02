/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.examples.calculator.expression;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.examples.calculator.CalculatorException;
import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.SimpleEnvironment;
import org.jmock.examples.calculator.expression.Literal;


public abstract class AbstractBinaryOperatorTest
        extends MockObjectTestCase
{
    SimpleEnvironment environment;
    private Mock left;
    private Mock right;

    public void setUp() {
        environment = new SimpleEnvironment();
        left = mock(Expression.class, "left");
        right = mock(Expression.class, "right");
    }

    protected void runOperatorTest() throws Exception {
        for (double i = 1; i <= 10; i = i + 1) {
            for (double j = 1; j <= 10; j = j + 1) {
                Expression expression = makeExpression(i, j);

                assertEquals(expectedValue(i, j),
                             expression.evaluate(environment),
                             0.0);
            }
        }
    }

    public void testReportsErrorsInLeftSubexpression() {
        Expression expression = makeExpression((Expression)left.proxy(),
                                               (Expression)right.proxy());

        CalculatorException thrown =
                new CalculatorException("thrown exception");

        left.expects(once()).method("evaluate").with(same(environment))
                .will(throwException(thrown));

        try {
            expression.evaluate(environment);
            fail("CalculatorException expected");
        }
        catch (CalculatorException caught) {
            assertSame("should be thrown exception", thrown, caught);
        }
    }

    public void testReportsErrorsInRightSubexpression() {
        Expression expression = makeExpression((Expression)left.proxy(),
                                               (Expression)right.proxy());

        CalculatorException thrown =
                new CalculatorException("thrown exception");

        left.expects(once()).method("evaluate").with(same(environment))
                .will(returnValue(0.0));
        right.expects(once()).method("evaluate").with(same(environment))
                .will(throwException(thrown));

        try {
            expression.evaluate(environment);
            fail("CalculatorException expected");
        }
        catch (CalculatorException caught) {
            assertSame("should be thrown exception", thrown, caught);
        }
    }

    protected Expression makeExpression( double leftLiteral, double rightLiteral ) {
        return makeExpression(new Literal(leftLiteral), new Literal(rightLiteral));
    }

    protected abstract Expression makeExpression( Expression leftExpression,
                                                  Expression rightExpression );

    protected abstract double expectedValue( double leftValue, double rightValue );

}
