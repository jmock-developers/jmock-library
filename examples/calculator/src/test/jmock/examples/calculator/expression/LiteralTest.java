/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.examples.calculator.expression;

import junit.framework.TestCase;
import org.jmock.examples.calculator.SimpleEnvironment;
import org.jmock.examples.calculator.expression.Literal;


public class LiteralTest extends TestCase
{
    public void testEvaluatesToValuePassedToConstructor() {
        SimpleEnvironment environment = new SimpleEnvironment();

        for (int i = 0; i <= 10; i++) {
            assertEquals(i, (new Literal(i)).evaluate(environment), 0.0);
        }
    }
}
