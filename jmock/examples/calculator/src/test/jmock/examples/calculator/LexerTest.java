/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.examples.calculator;

import junit.framework.TestCase;
import org.jmock.examples.calculator.Lexer;
import org.jmock.examples.calculator.Token;


public class LexerTest
        extends TestCase
{
    public void testHasAStackOfPushBackTokens() throws Exception {
        Lexer lexer = new Lexer("");
        lexer.pushBack(new Token(1, "1"));
        lexer.pushBack(new Token(2, "2"));

        assertNextToken(lexer, 2, "2");
        assertNextToken(lexer, 1, "1");
        assertNextToken(lexer, Token.END, null);
    }

    public void testTokenisesLiteralIntegerValues() throws Exception {
        String input = "23";
        Lexer lexer = new Lexer(input);

        assertNextToken(lexer, Token.NUMBER, "23");
        assertNextToken(lexer, Token.END, null);
    }

    public void testTokenisesLiteralRealValues() throws Exception {
        String input = "123.45";
        Lexer lexer = new Lexer(input);

        assertNextToken(lexer, Token.NUMBER, "123.45");
        assertNextToken(lexer, Token.END, null);
    }

    public void testTokenisesAddOperator() throws Exception {
        Lexer lexer = new Lexer("1+2");

        assertNextToken(lexer, Token.NUMBER, "1");
        assertNextToken(lexer, Token.ADD, "+");
        assertNextToken(lexer, Token.NUMBER, "2");
        assertNextToken(lexer, Token.END, null);
    }

    public void testTokenisesSubtractOperator() throws Exception {
        Lexer lexer = new Lexer("1-2");

        assertNextToken(lexer, Token.NUMBER, "1");
        assertNextToken(lexer, Token.SUBTRACT, "-");
        assertNextToken(lexer, Token.NUMBER, "2");
        assertNextToken(lexer, Token.END, null);
    }

    public void testTokenisesMultiplyOperator() throws Exception {
        Lexer lexer = new Lexer("1*2");

        assertNextToken(lexer, Token.NUMBER, "1");
        assertNextToken(lexer, Token.MULTIPLY, "*");
        assertNextToken(lexer, Token.NUMBER, "2");
        assertNextToken(lexer, Token.END, null);
    }

    public void testTokenisesDivideOperator() throws Exception {
        Lexer lexer = new Lexer("1/2");

        assertNextToken(lexer, Token.NUMBER, "1");
        assertNextToken(lexer, Token.DIVIDE, "/");
        assertNextToken(lexer, Token.NUMBER, "2");
        assertNextToken(lexer, Token.END, null);
    }

    public void testTokenisesPowerOperator() throws Exception {
        Lexer lexer = new Lexer("1^2");

        assertNextToken(lexer, Token.NUMBER, "1");
        assertNextToken(lexer, Token.POWER, "^");
        assertNextToken(lexer, Token.NUMBER, "2");
        assertNextToken(lexer, Token.END, null);
    }

    public void testTokenisesIdentifiers() throws Exception {
        Lexer lexer = new Lexer("identifier anotherIdentifier-identifier3");

        assertNextToken(lexer, Token.IDENTIFIER, "identifier");
        assertNextToken(lexer, Token.IDENTIFIER, "anotherIdentifier");
        assertNextToken(lexer, Token.SUBTRACT, "-");
        assertNextToken(lexer, Token.IDENTIFIER, "identifier3");
        assertNextToken(lexer, Token.END, null);
    }

    public void testSkipsWhitespaceBetweenTokens() throws Exception {
        String input = " 2 \t\n\r + 3";
        Lexer lexer = new Lexer(input);

        assertNextToken(lexer, Token.NUMBER, "2");
        assertNextToken(lexer, Token.ADD, "+");
        assertNextToken(lexer, Token.NUMBER, "3");
        assertNextToken(lexer, Token.END, null);
    }

    public void testTokenisesParentheses() throws Exception {
        Lexer lexer = new Lexer("(x+y)*z");

        assertNextToken(lexer, Token.PAREN_OPEN, "(");
        assertNextToken(lexer, Token.IDENTIFIER, "x");
        assertNextToken(lexer, Token.ADD, "+");
        assertNextToken(lexer, Token.IDENTIFIER, "y");
        assertNextToken(lexer, Token.PAREN_CLOSE, ")");
        assertNextToken(lexer, Token.MULTIPLY, "*");
        assertNextToken(lexer, Token.IDENTIFIER, "z");
        assertNextToken(lexer, Token.END, null);
    }

    private void assertNextToken( Lexer lexer, int tokenType, String tokenValue )
            throws Exception {
        Token token = lexer.nextToken();

        assertEquals("token type", tokenType, token.getType());
        assertEquals("token value", tokenValue, token.getValue());
    }
}
