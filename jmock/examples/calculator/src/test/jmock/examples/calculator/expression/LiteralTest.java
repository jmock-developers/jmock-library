/* Copyright Dec 7, 2003 Nat Pryce, all rights reserved.
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
