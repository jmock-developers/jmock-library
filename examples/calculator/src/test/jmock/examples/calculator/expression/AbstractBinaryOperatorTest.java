/* Copyright Dec 7, 2003 Nat Pryce, all rights reserved.
 */
package test.jmock.examples.calculator.expression;

import org.jmock.builder.Mock;
import org.jmock.builder.MockObjectTestCase;
import org.jmock.examples.calculator.CalculatorException;
import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.SimpleEnvironment;
import org.jmock.examples.calculator.expression.Literal;

public abstract class AbstractBinaryOperatorTest 
    extends MockObjectTestCase
{
    SimpleEnvironment environment;
    private Mock mockLeft;
    private Mock mockRight;

    public void setUp() {
        environment = new SimpleEnvironment();
        mockLeft = new Mock(Expression.class, "mockLeft");
        mockRight = new Mock(Expression.class, "mockRight");
    }

    protected void runOperatorTest() throws Exception {
        for (double i = 1; i <= 10; i = i + 1) {
            for (double j = 1; j <= 10; j = j + 1) {
                Expression expression = makeExpression( i, j );

                assertEquals(
                    expectedValue(i, j),
                    expression.evaluate(environment),
                    0.0);
            }
        }
    }

    public void testReportsErrorsInLeftSubexpression() {
        Expression expression =
            makeExpression(
                (Expression)mockLeft.proxy(),
                (Expression)mockRight.proxy());

        CalculatorException thrown =
            new CalculatorException("thrown exception");

        mockLeft.method("evaluate").with(eq(environment)).will(throwException(thrown))
            .expect(once());

        try {
            expression.evaluate(environment);
            fail("CalculatorException expected");
        } catch (CalculatorException caught) {
            assertSame("should be thrown exception", thrown, caught);
        }
    }

    public void testReportsErrorsInRightSubexpression() {
        Expression expression =
            makeExpression(
                (Expression)mockLeft.proxy(),
                (Expression)mockRight.proxy());

        CalculatorException thrown =
            new CalculatorException("thrown exception");
        
        mockLeft.method("evaluate").with(eq(environment)).will(returnValue(0.0))
            .expect(once());
        mockRight.method("evaluate").with(eq(environment)).will(throwException(thrown))
            .expect(once());
        
        try {
            expression.evaluate(environment);
            fail("CalculatorException expected");
        } catch (CalculatorException caught) {
            assertSame("should be thrown exception", thrown, caught);
        }
    }
    
    protected Expression makeExpression( double leftLiteral, double rightLiteral ) {
        return makeExpression( new Literal(leftLiteral), new Literal(rightLiteral) );
    }
    
    protected abstract Expression makeExpression(
        Expression left,
        Expression right);

    protected abstract double expectedValue(double left, double right);

}
