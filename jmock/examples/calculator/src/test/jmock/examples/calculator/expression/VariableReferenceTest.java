/* Copyright Dec 8, 2003 Nat Pryce, all rights reserved.
 */
package test.jmock.examples.calculator.expression;

import org.jmock.builder.Mock;
import org.jmock.builder.MockObjectTestCase;
import org.jmock.examples.calculator.CalculatorException;
import org.jmock.examples.calculator.Environment;
import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.SimpleEnvironment;
import org.jmock.examples.calculator.expression.VariableReference;


public class VariableReferenceTest extends MockObjectTestCase {
    
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
        
        mockEnvironment.method("getVariable").with(eq(variableName))
            .willReturn(mockDefinition.proxy())
            .expectOnce();
        mockDefinition.method("evaluate").with(same(mockEnvironment.proxy()))
            .willReturn(new Double(result))
            .expectOnce();
        
        assertEquals( "should be result of evaluating variable definition", 
            result, 
            variableReference.evaluate( (Environment)mockEnvironment.proxy() ), 
            0 );
    }
    
    public void testPassesBackExceptionsFromVariableDefiniton() {
        SimpleEnvironment environment = new SimpleEnvironment();
        environment.setVariable( variableName, (Expression)mockDefinition.proxy() );
        CalculatorException thrown = new CalculatorException("THROWN EXCEPTION");
        
        mockDefinition.method("evaluate").with(eq(environment)).willThrow(thrown)
            .expectOnce();
        
        try {
            variableReference.evaluate(environment);
            fail("expected CalculatorException to be thrown");
        }
        catch( CalculatorException caught ) {
            assertSame( "should be thrown exception", thrown, caught );
        }
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
    }
}
