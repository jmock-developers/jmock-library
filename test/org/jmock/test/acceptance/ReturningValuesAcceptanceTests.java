/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.acceptance;

import java.util.Date;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;


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
        void voidMethod();
    }

    private Mockery context = new Mockery();
    private ReturnTypes mock = context.mock(ReturnTypes.class, "mock");

    public void testCanReturnObjectReferences() {
        // ensure string is not interned
        final String result = new String("RESULT");

        context.checking(new Expectations() {{
            allowing(mock).returnString();
            will(returnValue(result));
        }});

        assertSame("should be same result", result, mock.returnString());
    }

    public void testCanReturnNullObjectReferences() {
        context.checking(new Expectations() {{
            allowing(mock).returnString(); will(returnValue(null));
        }});

        assertNull("should be null", mock.returnString());
    }

    public void testCanReturnBooleanValues() {
        context.checking(new Expectations() {{
            exactly(1).of(mock).returnBoolean(); will(returnValue(true));
            exactly(1).of(mock).returnBoolean(); will(returnValue(false));
        }});
        
        assertTrue("should be true", mock.returnBoolean());
        assertFalse("should be false", mock.returnBoolean());
    }

    public void testCanReturnByteValues() {
        final byte result = 123;

        context.checking(new Expectations() {{
            allowing(mock).returnByte(); will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnByte());
    }

    public void testCanReturnCharValues() {
        final char result = '\u1234';

        context.checking(new Expectations() {{
            allowing(mock).returnChar(); will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnChar());
    }

    public void testCanReturnShortValues() {
        final short result = 12345;

        context.checking(new Expectations() {{
            allowing(mock).returnShort(); will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnShort());
    }

    public void testCanReturnIntValues() {
        final int result = 1234567890;

        context.checking(new Expectations() {{
            allowing(mock).returnInt(); will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnInt());
    }

    public void testCanReturnLongValues() {
        final long result = 1234567890124356789L;

        context.checking(new Expectations() {{
            allowing(mock).returnLong(); will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnLong());
    }

    public void testCanReturnFloatValues() {
        final float result = 12345.67890f;

        context.checking(new Expectations() {{
            allowing(mock).returnFloat(); will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnFloat(), 0.0);
    }

    public void testCanReturnDoubleValues() {
        final double result = 1234567890.1234567890;

        context.checking(new Expectations() {{
            allowing (mock).returnDouble(); will(returnValue(result));
        }});

        assertEquals("should be same result", result, mock.returnDouble(), 0.0);
    }
    
    public void testWillReturnADefaultValueIfNoResultExplicitlySpecified() {
        context.checking(new Expectations() {{
            allowing (mock).returnInt();
        }});
        
        // This will not throw a NullPointerException
        mock.returnInt();
    }

    public class Something {}
    
    public interface AnInterfaceThatReturnsSomething {
        Something returnSomething();
    }
    
    public void testReturnsNullAsTheDefaultValueForUnregisteredType() {
        final AnInterfaceThatReturnsSomething returningMock = context.mock(AnInterfaceThatReturnsSomething.class, "returningMock");
        
        context.checking(new Expectations() {{
            allowing (returningMock).returnSomething();
        }});
        
        Something defaultResult = returningMock.returnSomething();
        
        assertNull("returned null", defaultResult);
    }
    
    public void testCanDefineDefaultReturnValuesForUnregisteredTypes() {
        final AnInterfaceThatReturnsSomething returningMock = context.mock(AnInterfaceThatReturnsSomething.class, "returningMock");
        
        Something expectedDefaultResult = new Something();
        
        context.setDefaultResultForType(Something.class, expectedDefaultResult);
        
        context.checking(new Expectations() {{
            allowing (returningMock).returnSomething();
        }});
        
        Something defaultResult = returningMock.returnSomething();
        
        assertSame("returned the default result", expectedDefaultResult, defaultResult);
    }
    
    public void testCanChangeDefaultReturnValueForRegisteredType() {
        String newDefaultString = "hoo-hee-haa-haa";
        context.setDefaultResultForType(String.class, newDefaultString);
        
        context.checking(new Expectations() {{
            allowing (mock).returnString();
        }});
        
        assertSame("returned the default result", newDefaultString, mock.returnString());
    }

    public void testReportsTypeMismatchOfResults() {
        try {
            context.checking(new Expectations() {{
                allowing (mock).returnString(); will(returnValue(new Date()));
            }});
            
            mock.returnString();
            fail("Should have thrown IllegalStateException");

        } catch (IllegalStateException expected) {
        }
    }
    
    public void testReportsTypeMismatchWhenValuesReturnedFromVoidMethods() {
        context.checking(new Expectations() {{
            allowing (mock).voidMethod(); will(returnValue("wrong result"));
        }});
        
       try {
           mock.voidMethod();
           fail("Should have thrown IllegalStateException");

       } catch (IllegalStateException expected) {
       }
    }
    
}
