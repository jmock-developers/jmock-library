/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.examples.calculator;


import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
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
        mockExpressionFactory = mock(ExpressionFactory.class);
        parser = new InfixParser((ExpressionFactory)mockExpressionFactory.proxy());
    }

    public void testParsesLiteral() throws Exception {
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(4.0))
                .will(returnValue(mockLiteral1));

        assertSame("should be literal", mockLiteral1, parser.parse("4.0"));
    }

    public void testParsesVariableReference() throws Exception {
        mockExpressionFactory.expects(once()).method("newVariableReference").with(eq("varName"))
                .will(returnValue(mockVariableReference));

        assertSame("should be variable reference",
                   mockVariableReference, parser.parse("varName"));
    }

    public void testParsesAddition() throws Exception {
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(1.0))
                .will(returnValue(mockLiteral1));
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(2.0))
                .will(returnValue(mockLiteral2));
        mockExpressionFactory.expects(once()).method("newAddition")
                .with(same(mockLiteral1), same(mockLiteral2))
                .will(returnValue(mockAddition));

        assertSame("should be addition", mockAddition, parser.parse("1+2"));
    }

    public void testThrowsExceptionForInvalidAdditionSyntax() throws Exception {
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(1.0))
                .will(returnValue(mockLiteral1));

        try {
            parser.parse("1+");
            fail("ParseException expected when missing rhs");
        }
        catch (ParseException expected) { /* expected */
        }

        try {
            parser.parse("+2");
            fail("ParseException expected when missing lhs");
        }
        catch (ParseException expected) { /* expected */
        }
    }

    public void testParsesSubtraction() throws Exception {
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(1.0))
                .will(returnValue(mockLiteral1));
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(2.0))
                .will(returnValue(mockLiteral2));
        mockExpressionFactory.expects(once()).method("newSubtraction").with(same(mockLiteral1), same(mockLiteral2))
                .will(returnValue(mockSubtraction));

        assertSame("should be addition", mockSubtraction, parser.parse("1-2"));
    }

    public void testThrowsExceptionForInvalidSubtractionSyntax() throws Exception {
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(1.0))
                .will(returnValue(mockLiteral1));

        try {
            parser.parse("1-");
            fail("ParseException expected when missing rhs");
        }
        catch (ParseException expected) { /* expected */
        }

        try {
            parser.parse("-2");
            fail("ParseException expected when missing lhs");
        }
        catch (ParseException expected) { /* expected */
        }
    }

    public void testParsesMultiplication() throws Exception {
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(1.0))
                .will(returnValue(mockLiteral1));
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(2.0))
                .will(returnValue(mockLiteral2));
        mockExpressionFactory.expects(once()).method("newMultiplication")
                .with(same(mockLiteral1), same(mockLiteral2))
                .will(returnValue(mockMultiplication));

        assertSame("should be multiplication", mockMultiplication, parser.parse("1*2"));
    }

    public void testParsesDivision() throws Exception {
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(1.0))
                .will(returnValue(mockLiteral1));
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(2.0))
                .will(returnValue(mockLiteral2));
        mockExpressionFactory.expects(once()).method("newDivision").with(same(mockLiteral1), same(mockLiteral2))
                .will(returnValue(mockDivision));

        assertSame("should be division", mockDivision, parser.parse("1/2"));
    }

    public void testParsesPower() throws Exception {
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(1.0))
                .will(returnValue(mockLiteral1));
        mockExpressionFactory.expects(once()).method("newLiteral").with(eq(2.0))
                .will(returnValue(mockLiteral2));
        mockExpressionFactory.expects(once()).method("newPower").with(same(mockLiteral1), same(mockLiteral2))
                .will(returnValue(mockPower));

        assertSame("should be power", mockPower, parser.parse("1^2"));
    }

    public void testParseParenthesis() throws Exception {
        Expression xReference = dummyExpression("xReference");
        Expression yReference = dummyExpression("yReference");
        Expression zReference = dummyExpression("zReference");
        Expression addition = dummyExpression("addition");
        Expression multiplication = dummyExpression("multiplication");

        mockExpressionFactory.expects(once()).method("newVariableReference").with(eq("x"))
                .will(returnValue(xReference));
        mockExpressionFactory.expects(once()).method("newVariableReference").with(eq("y"))
                .will(returnValue(yReference));
        mockExpressionFactory.expects(once()).method("newVariableReference").with(eq("z"))
                .will(returnValue(zReference));
        mockExpressionFactory.expects(once()).method("newAddition").with(same(xReference), same(yReference))
                .will(returnValue(addition));
        mockExpressionFactory.expects(once()).method("newMultiplication").with(same(addition), same(zReference))
                .will(returnValue(multiplication));

        assertSame("should be multiplication",
                   multiplication, parser.parse("(x+y)*z"));
    }

    public void testReportsUnexpectedEndOfInput() {
        try {
            parser.parse("");
            fail("ParseException expected");
        }
        catch (ParseException ex) {
            assertTrue("error message should report unexpected end of input",
                       ex.getMessage().indexOf("unexpected end of input") >= 0);
        }
    }

    public void testReportsUnexpectedToken() {
        String wrongOperator = "*";

        try {
            parser.parse(wrongOperator);
            fail("ParseException expected");
        }
        catch (ParseException ex) {
            assertTrue("error message should include unexpected token",
                       ex.getMessage().indexOf(wrongOperator) >= 0);
        }
    }

    public void testReportsMissingClosingParenthesis() throws Exception {
        Expression xReference = dummyExpression("xReference");

        mockExpressionFactory.expects(once()).method("newVariableReference").with(eq("x"))
                .will(returnValue(xReference));

        try {
            parser.parse("(x");
            fail("ParseException expected");
        }
        catch (ParseException expected) { /* expected */
        }
    }

    private Expression dummyExpression( String name ) {
        return (Expression)Dummy.newDummy(Expression.class, name);
    }
}
