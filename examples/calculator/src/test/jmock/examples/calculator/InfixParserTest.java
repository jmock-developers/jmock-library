/* Copyright Dec 7, 2003 Nat Pryce, all rights reserved.
 */
package test.jmock.examples.calculator;


import org.jmock.builder.Mock;
import org.jmock.builder.MockObjectTestCase;
import org.jmock.examples.calculator.Expression;
import org.jmock.examples.calculator.ExpressionFactory;
import org.jmock.examples.calculator.InfixParser;
import org.jmock.examples.calculator.ParseException;
import org.jmock.util.Dummy;


public class InfixParserTest 
    extends MockObjectTestCase 
{
    private Mock mockExpressionFactory;
    private InfixParser parser;
    private Expression mockLiteral1 = dummyExpression("mockLiteral1");
    private Expression mockLiteral2 = dummyExpression("mockLiteral2");
    private Expression mockAddition = dummyExpression("mockAddition");
    private Expression mockSubtraction = dummyExpression("mockSubtraction");
    private Expression mockMultiplication = dummyExpression("mockMultiplication");
    private Expression mockDivision = dummyExpression("mockDivision");
    private Expression mockPower = dummyExpression("mockPower");
    private Expression mockVariableReference = dummyExpression("mockVariableReference");
    
    
    public void setUp() {
        mockExpressionFactory = new Mock(ExpressionFactory.class);
        parser = new InfixParser( (ExpressionFactory)mockExpressionFactory.proxy() );
    }
    
    public void testParsesLiteral() throws Exception {
        mockExpressionFactory.method("newLiteral").with(eq(4.0))
            .will(returnValue(mockLiteral1))
            .expectOnce();
        
        assertSame( "should be literal", mockLiteral1, parser.parse("4.0") );
    }
    
    public void testParsesVariableReference() throws Exception {
        mockExpressionFactory.method("newVariableReference").with(eq("varName"))
            .will(returnValue(mockVariableReference))
            .expectOnce();
        
        assertSame( "should be variable reference", 
                    mockVariableReference, parser.parse("varName") );
    }
    
    public void testParsesAddition() throws Exception {
        mockExpressionFactory.method("newLiteral").with(eq(1.0))
            .will(returnValue(mockLiteral1))
            .expectOnce();
        mockExpressionFactory.method("newLiteral").with(eq(2.0))
            .will(returnValue(mockLiteral2))
            .expectOnce();
        mockExpressionFactory.method("newAddition").with(same(mockLiteral1),same(mockLiteral2))
            .will(returnValue(mockAddition))
            .expectOnce();
        
        assertSame( "should be addition", mockAddition, parser.parse("1+2") );
    }

    public void testThrowsExceptionForInvalidAdditionSyntax() throws Exception {
        mockExpressionFactory.method("newLiteral").with(eq(1.0))
            .will(returnValue(mockLiteral1))
            .expectOnce();
        
        try {
            parser.parse("1+");
            fail("ParseException expected when missing rhs");
        }
        catch( ParseException expected ) { /* expected */ }
        
        try {
            parser.parse("+2");
            fail("ParseException expected when missing lhs");
        }
        catch( ParseException expected ) { /* expected */ }
    }

    public void testParsesSubtraction() throws Exception {
        mockExpressionFactory.method("newLiteral").with(eq(1.0))
            .will(returnValue(mockLiteral1))
            .expectOnce();
        mockExpressionFactory.method("newLiteral").with(eq(2.0))
            .will(returnValue(mockLiteral2))
            .expectOnce();
        mockExpressionFactory.method("newSubtraction").with(same(mockLiteral1),same(mockLiteral2))
            .will(returnValue(mockSubtraction))
            .expectOnce();
        
        assertSame( "should be addition", mockSubtraction, parser.parse("1-2") );
    }
    
    public void testThrowsExceptionForInvalidSubtractionSyntax() throws Exception {
        mockExpressionFactory.method("newLiteral").with(eq(1.0))
            .will(returnValue(mockLiteral1))
            .expectOnce();
        
        try {
            parser.parse("1-");
            fail("ParseException expected when missing rhs");
        }
        catch( ParseException expected ) { /* expected */ }

        try {
            parser.parse("-2");
            fail("ParseException expected when missing lhs");
        }
        catch( ParseException expected ) { /* expected */ }
    }
    
    public void testParsesMultiplication() throws Exception {
        mockExpressionFactory.method("newLiteral").with(eq(1.0)).will(returnValue(mockLiteral1))
            .expectOnce();
        mockExpressionFactory.method("newLiteral").with(eq(2.0)).will(returnValue(mockLiteral2))
            .expectOnce();
        mockExpressionFactory.method("newMultiplication").with(same(mockLiteral1),same(mockLiteral2))
            .will(returnValue(mockMultiplication))
            .expectOnce();
        
        assertSame( "should be multiplication", mockMultiplication, parser.parse("1*2") );
    }
    
    public void testParsesDivision() throws Exception {
        mockExpressionFactory.method("newLiteral").with(eq(1.0)).will(returnValue(mockLiteral1))
            .expectOnce();
        mockExpressionFactory.method("newLiteral").with(eq(2.0)).will(returnValue(mockLiteral2))
            .expectOnce();
        mockExpressionFactory.method("newDivision").with(same(mockLiteral1),same(mockLiteral2))
            .will(returnValue(mockDivision))
            .expectOnce();
        
        assertSame( "should be division", mockDivision, parser.parse("1/2") );
    }
    
    public void testParsesPower() throws Exception {
        mockExpressionFactory.method("newLiteral").with(eq(1.0)).will(returnValue(mockLiteral1))
            .expectOnce();
        mockExpressionFactory.method("newLiteral").with(eq(2.0)).will(returnValue(mockLiteral2))
            .expectOnce();
        mockExpressionFactory.method("newPower").with(same(mockLiteral1),same(mockLiteral2))
            .will(returnValue(mockPower))
            .expectOnce();
        
        assertSame( "should be power", mockPower, parser.parse("1^2") );
    }
    
    public void testParseParenthesis() throws Exception {
        Expression xReference = dummyExpression("xReference");
        Expression yReference = dummyExpression("yReference");
        Expression zReference = dummyExpression("zReference");
        Expression addition = dummyExpression("addition");
        Expression multiplication = dummyExpression("multiplication");
        
        mockExpressionFactory.method("newVariableReference").with(eq("x"))
            .will(returnValue(xReference))
            .expectOnce();
        mockExpressionFactory.method("newVariableReference").with(eq("y"))
            .will(returnValue(yReference))
            .expectOnce();
        mockExpressionFactory.method("newVariableReference").with(eq("z"))
            .will(returnValue(zReference))
            .expectOnce();
        mockExpressionFactory.method("newAddition").with(same(xReference),same(yReference))
            .will(returnValue(addition))
            .expectOnce();
        mockExpressionFactory.method("newMultiplication").with(same(addition),same(zReference))
            .will(returnValue(multiplication))
            .expectOnce();
        
        assertSame( "should be multiplication", 
                    multiplication, parser.parse("(x+y)*z") );
    }
    
    public void testReportsUnexpectedEndOfInput() {
    	try {
    		parser.parse("");
            fail("ParseException expected");
        }
        catch( ParseException ex ) {
        	assertTrue( "error message should report unexpected end of input",
                        ex.getMessage().indexOf("unexpected end of input") >= 0 );
        }
    }
    
    public void testReportsUnexpectedToken() {
        String wrongOperator = "*";
        
		try {
            parser.parse(wrongOperator);
            fail("ParseException expected");
        }
        catch( ParseException ex ) {
            assertTrue( "error message should include unexpected token",
                ex.getMessage().indexOf(wrongOperator) >= 0 );
        }
    }
    
    public void testReportsMissingClosingParenthesis() throws Exception {
        Expression xReference = dummyExpression("xReference");
        
        mockExpressionFactory.method("newVariableReference").with(eq("x"))
            .will(returnValue(xReference))
            .expectOnce();
        
        try {
            parser.parse("(x");
            fail("ParseException expected");
        }
        catch( ParseException expected ) { /* expected */ }
    }
    
    private Expression dummyExpression( String name ) {
        return (Expression)Dummy.newDummy( Expression.class, name );
    }
}
