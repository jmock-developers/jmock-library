/* Copyright Dec 6, 2003 Nat Pryce, all rights reserved.
 */
package test.jmock.examples.calculator;

import org.jmock.dynamock.Mock;

import junit.framework.TestCase;


import org.jmock.examples.calculator.CalculatorException;
import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.SimpleEnvironment;


public class SimpleEnvironmentTest extends TestCase {
    private final String variableName = "<VARIABLE NAME>";

    private SimpleEnvironment environment;

    public void setUp() {
        environment = new SimpleEnvironment();
    }

    public void testThrowsExceptionIfVariableNotInEnvironment() {
        try {
            environment.getVariable(variableName);
            fail("CalculatorException expected");
        } catch (CalculatorException ex) {
            assertTrue(
                "error message should contain name of variable",
                ex.getMessage().indexOf(variableName) >= 0);
        }
    }

    public void testStoresVariableDefinitionByName() throws Exception {
        String nameX = "<NAME X>";
        Mock mockExpressionX = new Mock(Expression.class, "mockExpressionX");
        String nameY = "<NAME Y>";
        Mock mockExpressionY = new Mock(Expression.class, "mockExpressionY");

        environment.setVariable(nameX, (Expression)mockExpressionX.proxy());
        environment.setVariable(nameY, (Expression)mockExpressionY.proxy());

        assertSame(
            "should be variable definition X",
            mockExpressionX.proxy(),
            environment.getVariable(nameX));
        assertSame(
            "should be variable definition Y",
            mockExpressionY.proxy(),
            environment.getVariable(nameY));
    }
}
