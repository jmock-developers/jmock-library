/* Copyright Dec 6, 2003 Nat Pryce, all rights reserved.
 */
package test.jmock.examples.calculator;

import org.jmock.builder.Mock;
import org.jmock.builder.MockObjectTestCase;
import org.jmock.examples.calculator.Calculator;
import org.jmock.examples.calculator.CalculatorException;
import org.jmock.examples.calculator.Environment;
import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.ParseException;
import org.jmock.examples.calculator.Parser;

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
        mockExpression = new Mock(Expression.class);
        mockParser = new Mock(Parser.class);
        mockEnvironment = new Mock(Environment.class);
        mockVariableExpression = new Mock(Expression.class,"mockVariableExpression");
        
        calculator = new Calculator( (Parser)mockParser.proxy(),
                                     (Environment)mockEnvironment.proxy() );
    }
    
    public void testParsesAndCalculatesExpression() throws Exception {
        final double expressionValue = 1.0;
        
        mockParser.method("parse").with(eq(expressionString))
            .will(returnValue(mockExpression.proxy()))
            .expect(once());
        mockExpression.method("evaluate").with(same(mockEnvironment.proxy()))
            .will(returnValue(expressionValue))
            .expect(once());
        
        assertEquals( "should be expression value",
                      expressionValue, calculator.calculate(expressionString), 0.0 ); 
    }
    
    public void testReportsParseErrors() throws Exception {
        final Throwable throwable = new ParseException("dummy ParseException");
        
        mockParser.method("parse").with(eq(expressionString)).will(throwException(throwable))
            .expect(once()); 
        
        try {
            calculator.calculate(expressionString);
            fail("ParseException expected");
        }
        catch( ParseException ex ) {
            // expected
        } 
    }
    
    public void testReportsEvaluationErrors() throws Exception {
        final Throwable throwable = new CalculatorException("dummy CalculatorException");
        
        mockParser.method("parse").with(eq(expressionString))
            .will(returnValue(mockExpression.proxy()))
            .expect(once());
        mockExpression.method("evaluate").with(same(mockEnvironment.proxy()))
            .will(throwException(throwable))
            .expect(once());
        
        try {
            calculator.calculate(expressionString);
            fail("CalculatorException expected");
        }
        catch( CalculatorException ex ) {
            // expected
        }
    }
    
    public void testSetsVariableExpression() throws Throwable {
        mockParser.method("parse").with(eq(variableValueString))
            .will(returnValue(mockVariableExpression.proxy()))
            .expect(once());
        
        mockEnvironment.method("setVariable")
            .with( eq(variableName), same(mockVariableExpression.proxy()) )
            .expect(once());
        
        calculator.setVariable( variableName, variableValueString );
    }
}

