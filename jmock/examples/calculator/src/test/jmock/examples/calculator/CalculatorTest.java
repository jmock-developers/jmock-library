/* Copyright Dec 6, 2003 Nat Pryce, all rights reserved.
 */
package test.jmock.examples.calculator;

import org.jmock.C;
import org.jmock.dynamock.Mock;
import org.jmock.expectation.Verifier;

import org.jmock.examples.calculator.*;

import junit.framework.TestCase;

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
        
        mockParser.expectAndReturn( "parse", C.args(C.eq(expressionString)), 
                                    (Expression)mockExpression.proxy() );
        mockExpression.expectAndReturn( "evaluate", C.args(C.same(mockEnvironment.proxy())), 
                                        new Double(expressionValue) );
        
        assertEquals( "should be expression value",
                      expressionValue, calculator.calculate(expressionString), 0.0 ); 
        
        verifyAll();
    }
    
    public void testReportsParseErrors() throws Exception {
        final Throwable throwable = new ParseException("dummy ParseException");
        
        mockParser.expectAndThrow( "parse", C.args(C.eq(expressionString)), 
                                   throwable );
        
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
        
        mockParser.expectAndReturn( "parse", C.args(C.eq(expressionString)), 
                                    mockExpression.proxy() );
        mockExpression.expectAndThrow( "evaluate", C.args(C.same(mockEnvironment.proxy())),
                                       throwable );
        
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
        mockParser.expectAndReturn( "parse", C.args(C.eq(variableValueString)),
                                    (Expression)mockVariableExpression.proxy() );
        
        mockEnvironment.expectVoid( "setVariable",
            C.eq( variableName, (Expression)mockVariableExpression.proxy() ) );
        
        calculator.setVariable( variableName, variableValueString );
        
        verifyAll();
    }
}

