/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.examples.calculator;

import junit.framework.TestCase;
import org.jmock.examples.calculator.Token;


public class TokenTest extends TestCase
{
    public void testStoresTokenTypeAndStringValue() {
        int tokenType = 1000;
        String tokenValue = "<TOKEN VALUE>";

        Token token = new Token(tokenType, tokenValue);

        assertEquals("should be token type", tokenType, token.getType());
        assertEquals("should be token value", tokenValue, token.getValue());
    }
}
