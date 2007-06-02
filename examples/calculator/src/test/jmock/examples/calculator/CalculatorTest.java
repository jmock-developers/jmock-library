/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.examples.calculator;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.examples.calculator.*;


public class CalculatorTest
        extends MockObjectTestCase
{
    private Mock mockExpression;
    private Mock mockParser;
    private Mock mockEnvironment;
    private Calculator calculator;
    private final String expressionString = "<EXPRESSION>";
    private final String variableName = "<VARIABLE NAME>";
    private final String variableValueString = "<VARIABLE VALUE>";
    private Mock mockVariableExpression;

    public void setUp() {
        mockExpression = mock(Expression.class);
        mockParser = mock(Parser.class);
        mockEnvironment = mock(Environment.class);
        mockVariableExpression = mock(Expression.class, "mockVariableExpression");

        calculator = new Calculator((Parser)mockParser.proxy(),
                                    (Environment)mockEnvironment.proxy());
    }

    public void testParsesAndCalculatesExpression() throws Exception {
        final double expressionValue = 1.0;

        mockParser.expects(once()).method("parse").with(eq(expressionString))
                .will(returnValue(mockExpression.proxy()));
        mockExpression.expects(once()).method("evaluate").with(same(mockEnvironment.proxy()))
                .will(returnValue(expressionValue));

        assertEquals("should be expression value",
                     expressionValue, calculator.calculate(expressionString), 0.0);
    }

    public void testReportsParseErrors() throws Exception {
        final Throwable throwable = new ParseException("dummy ParseException");

        mockParser.expects(once()).method("parse").with(eq(expressionString))
                .will(throwException(throwable));

        try {
            calculator.calculate(expressionString);
            fail("ParseException expected");
        }
        catch (ParseException ex) {
            // expected
        }
    }

    public void testReportsEvaluationErrors() throws Exception {
        final Throwable throwable = new CalculatorException("dummy CalculatorException");

        mockParser.expects(once()).method("parse").with(eq(expressionString))
                .will(returnValue(mockExpression.proxy()));
        mockExpression.expects(once()).method("evaluate").with(same(mockEnvironment.proxy()))
                .will(throwException(throwable));

        try {
            calculator.calculate(expressionString);
            fail("CalculatorException expected");
        }
        catch (CalculatorException ex) {
            // expected
        }
    }

    public void testSetsVariableExpression() throws Throwable {
        mockParser.expects(once()).method("parse").with(eq(variableValueString))
                .will(returnValue(mockVariableExpression.proxy()));

        mockEnvironment.expects(once()).method("setVariable")
                .with(eq(variableName), same(mockVariableExpression.proxy()));

        calculator.setVariable(variableName, variableValueString);
    }
}

