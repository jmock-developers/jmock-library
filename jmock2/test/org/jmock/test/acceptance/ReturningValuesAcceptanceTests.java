/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.acceptance;

import org.jmock.InAnyOrder;
import org.jmock.Mockery;

import junit.framework.TestCase;


public class ReturningValuesAcceptanceTests extends TestCase {
    public interface ReturnTypes {
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

    private Mockery context = new Mockery();
    private ReturnTypes mock = context.mock(ReturnTypes.class, "mock");

    public void testCanReturnObjectReferences() {
        // ensure string is not interned
        final String result = new String("RESULT");

        context.expects(new InAnyOrder() {{
            allow(mock).returnString();
            will(returnValue(result));
        }});

        assertSame("should be same result", result, mock.returnString());
    }

    public void testCanReturnNullObjectReferences() {
        context.expects(new InAnyOrder() {{
            allow(mock).returnString();
            will(returnValue(null));
        }});

        assertNull("should be null", mock.returnString());
    }

    public void testCanReturnBooleanValues() {
        context.expects(new InAnyOrder() {{
            exactly(1).of(mock).returnBoolean(); will(returnValue(true));
            exactly(1).of(mock).returnBoolean(); will(returnValue(false));
        }});
        
        assertTrue("should be true", mock.returnBoolean());
        assertFalse("should be false", mock.returnBoolean());
    }

    public void testCanReturnByteValues() {
        final byte result = 123;

        context.expects(new InAnyOrder() {{
            allow(mock).returnByte();
            will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnByte());
    }

    public void testCanReturnCharValues() {
        final char result = '\u1234';

        context.expects(new InAnyOrder() {{
            allow(mock).returnChar();
            will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnChar());
    }

    public void testCanReturnShortValues() {
        final short result = 12345;

        context.expects(new InAnyOrder() {{
            allow(mock).returnShort();
            will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnShort());
    }

    public void testCanReturnIntValues() {
        final int result = 1234567890;

        context.expects(new InAnyOrder() {{
            allow(mock).returnInt();
            will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnInt());
    }

    public void testCanReturnLongValues() {
        final long result = 1234567890124356789L;

        context.expects(new InAnyOrder() {{
            allow(mock).returnLong();
            will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnLong());
    }

    public void testCanReturnFloatValues() {
        final float result = 12345.67890f;

        context.expects(new InAnyOrder() {{
            allow(mock).returnFloat();
            will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnFloat(), 0.0);
    }

    public void testCanReturnDoubleValues() {
        final double result = 1234567890.1234567890;

        context.expects(new InAnyOrder() {{
            allow (mock).returnDouble(); will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnDouble(), 0.0);
    }
    
    public void testWillReturnADefaultValueIfNoResultExplicitlySpecified() {
        context.expects(new InAnyOrder() {{
            allow (mock).returnInt();
        }});
        
        // This will not throw a NullPointerException
        mock.returnInt();
    }
}
