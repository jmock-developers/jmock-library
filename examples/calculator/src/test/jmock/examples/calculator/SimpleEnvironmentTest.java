/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.examples.calculator;

import junit.framework.TestCase;
import org.jmock.examples.calculator.CalculatorException;
import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.SimpleEnvironment;
import org.jmock.util.Dummy;


public class SimpleEnvironmentTest extends TestCase
{
    private final String variableName = "<VARIABLE NAME>";

    private SimpleEnvironment environment;

    public void setUp() {
        environment = new SimpleEnvironment();
    }

    public void testThrowsExceptionIfVariableNotInEnvironment() {
        try {
            environment.getVariable(variableName);
            fail("CalculatorException expected");
        }
        catch (CalculatorException ex) {
            assertTrue("error message should contain name of variable",
                       ex.getMessage().indexOf(variableName) >= 0);
        }
    }

    public void testStoresVariableDefinitionByName() throws Exception {
        String nameX = "<NAME X>";
        Expression dummyExpressionX = dummyExpression("dummyExpressionX");
        String nameY = "<NAME Y>";
        Expression dummyExpressionY = dummyExpression("dummyExpressionY");

        environment.setVariable(nameX, dummyExpressionX);
        environment.setVariable(nameY, dummyExpressionY);

        assertSame("should be variable definition X",
                   dummyExpressionX, environment.getVariable(nameX));
        assertSame("should be variable definition Y",
                   dummyExpressionY, environment.getVariable(nameY));
    }

    private Expression dummyExpression( String name ) {
        return (Expression)Dummy.newDummy(Expression.class, name);
    }
}
