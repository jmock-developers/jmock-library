/* Copyright Dec 8, 2003 Nat Pryce, all rights reserved.
 */
package test.jmock.examples.calculator.expression;

import org.jmock.dynamock.Mock;

import junit.framework.TestCase;


import org.jmock.examples.calculator.CalculatorException;
import org.jmock.examples.calculator.Environment;
import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.SimpleEnvironment;
import org.jmock.examples.calculator.expression.VariableReference;


public class VariableReferenceTest extends TestCase {
    
    private final String variableName = "VARIABLE NAME";
    private Mock mockDefinition;
    private VariableReference variableReference;
    
    public void setUp() {
        mockDefinition = new Mock(Expression.class,"mockDefinition");
        variableReference = new VariableReference(variableName);
    }

    public void testEvaluatesDefinitionOfReferencedVariable() throws Exception {
        Mock mockEnvironment = new Mock(Environment.class);
        double result = 1234;
        
        mockEnvironment.expectAndReturn( "getVariable", variableName, 
            mockDefinition.proxy() );
        mockDefinition.expectAndReturn( "evaluate", mockEnvironment.proxy(), 
            new Double(result) );
        
        assertEquals( "should be result of evaluating variable definition", 
            result, 
            variableReference.evaluate( (Environment)mockEnvironment.proxy() ), 
            0 );
            
        mockEnvironment.verify();
        mockDefinition.verify();
    }
    
    public void testPassesBackExceptionsFromVariableDefiniton() {
        SimpleEnvironment environment = new SimpleEnvironment();
        environment.setVariable( variableName, (Expression)mockDefinition.proxy() );
        CalculatorException thrown = new CalculatorException("THROWN EXCEPTION");
        
        mockDefinition.expectAndThrow( "evaluate", environment, thrown );
        
        try {
            variableReference.evaluate(environment);
            fail("expected CalculatorException to be thrown");
        }
        catch( CalculatorException caught ) {
            assertSame( "should be thrown exception", thrown, caught );
        }
        
        mockDefinition.verify();
    }
    
    public void testThrowsCalculatorExceptionIfVariableNotDefined() {
        SimpleEnvironment environment = new SimpleEnvironment();
        
        try {
            variableReference.evaluate(environment);
            fail("expected CalculatorException to be thrown");
        }
        catch( CalculatorException ex ) {
            assertTrue( "should contain variable name in message",
                        ex.getMessage().indexOf(variableName) >= 0 );
        }
        
        mockDefinition.verify();
    }
}
