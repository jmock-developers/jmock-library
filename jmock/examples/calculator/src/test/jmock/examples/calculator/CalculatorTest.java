/* Copyright Dec 6, 2003 Nat Pryce, all rights reserved.
 */
package test.jmock.examples.calculator;

import junit.framework.TestCase;

import org.jmock.builder.Mock;
import org.jmock.examples.calculator.Calculator;
import org.jmock.examples.calculator.CalculatorException;
import org.jmock.examples.calculator.Environment;
import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.ParseException;
import org.jmock.examples.calculator.Parser;
import org.jmock.util.Verifier;

public class CalculatorTest extends TestCase {
    
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
    
    private void verifyAll() {
        Verifier.verifyObject(this);
    }
    
    public void testParsesAndCalculatesExpression() throws Exception {
        final double expressionValue = 1.0;
        
        mockParser.method("parse").passed(expressionString)
            .willReturn(mockExpression.proxy())
            .expectOnce();
        mockExpression.method("evaluate").passed(mockEnvironment.proxy())
            .willReturn(new Double(expressionValue))
            .expectOnce();
        
        assertEquals( "should be expression value",
                      expressionValue, calculator.calculate(expressionString), 0.0 ); 
        
        verifyAll();
    }
    
    public void testReportsParseErrors() throws Exception {
        final Throwable throwable = new ParseException("dummy ParseException");
        
        mockParser.method("parse").passed(expressionString).willThrow(throwable)
            .expectOnce(); 
        
        try {
            calculator.calculate(expressionString);
            fail("ParseException expected");
        }
        catch( ParseException ex ) {
            // expected
        } 
        
        verifyAll();        
    }
    
    public void testReportsEvaluationErrors() throws Exception {
        final Throwable throwable = new CalculatorException("dummy CalculatorException");
        
        mockParser.method("parse").passed(expressionString).willReturn(mockExpression.proxy())
            .expectOnce();
        mockExpression.method("evaluate").passed(mockEnvironment.proxy()).willThrow(throwable)
            .expectOnce();
        
        try {
            calculator.calculate(expressionString);
            fail("CalculatorException expected");
        }
        catch( CalculatorException ex ) {
            // expected
        } 
        
        verifyAll();        
    }
    
    public void testSetsVariableExpression() throws Throwable {
        mockParser.method("parse").passed(variableValueString)
            .willReturn(mockVariableExpression.proxy())
            .expectOnce();
        
        mockEnvironment.method("setVariable").passed(variableName,mockVariableExpression.proxy())
            .expectOnce();
        
        calculator.setVariable( variableName, variableValueString );
        
        verifyAll();
    }
}

