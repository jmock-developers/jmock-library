/*  Copyright (c) 2000-2004 jMock.org
 */
package atest.jmock;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;


public class ReturnTypeAcceptanceTest extends MockObjectTestCase
{
    public interface ReturnTypes
    {
        String returnString();

        boolean returnBoolean();

        byte returnByte();

        char returnChar();

        short returnShort();

        int returnInt();

        long returnLong();

        float returnFloat();

        double returnDouble();
    }

    private Mock mock;
    private ReturnTypes proxy;

    public void setUp() {
        mock = mock(ReturnTypes.class, "theMock");
        proxy = (ReturnTypes)mock.proxy();
    }

    public void testCanReturnObjectReferences() {
        String result = new String("RESULT");

        mock.stubs().method("returnString").will(returnValue(result));

        assertSame("should be same result", result, proxy.returnString());
    }

    public void testCanReturnBooleanValues() {
        mock.stubs().method("returnBoolean").will(returnValue(true));
        assertTrue("should be true", proxy.returnBoolean());

        mock.stubs().method("returnBoolean").will(returnValue(false));
        assertFalse("should be false", proxy.returnBoolean());
    }

    public void testCanReturnByteValues() {
        byte result = 123;

        mock.stubs().method("returnByte").will(returnValue(result));

        assertEquals("should be same result", result, proxy.returnByte());
    }

    public void testCanReturnCharValues() {
        char result = '\u1234';

        mock.stubs().method("returnChar").will(returnValue(result));

        assertEquals("should be same result", result, proxy.returnChar());
    }

    public void testCanReturnShortValues() {
        short result = 12345;

        mock.stubs().method("returnShort").will(returnValue(result));

        assertEquals("should be same result", result, proxy.returnShort());
    }

    public void testCanReturnIntValues() {
        int result = 1234567890;

        mock.stubs().method("returnInt").will(returnValue(result));

        assertEquals("should be same result", result, proxy.returnInt());
    }

    public void testCanReturnLongValues() {
        long result = 1234567890124356789L;

        mock.stubs().method("returnLong").will(returnValue(result));

        assertEquals("should be same result", result, proxy.returnLong());
    }

    public void testCanReturnFloatValues() {
        float result = 12345.67890f;

        mock.stubs().method("returnFloat").will(returnValue(result));

        assertEquals("should be same result", result, proxy.returnFloat(), 0.0);
    }

    public void testCanReturnDoubleValues() {
        double result = 1234567890.1234567890;

        mock.stubs().method("returnDouble").will(returnValue(result));

        assertEquals("should be same result", result, proxy.returnDouble(), 0.0);
    }
}
