/*
 * Copyright (c) 2001-2004 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package atest.jmock.easy;

import junit.framework.TestCase;

import org.jmock.easy.MockControl;

public class DefaultReturnValueAcceptanceTest extends TestCase {
    MockControl control;
    IMethods mock;

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testDefaultReturnValue() {
        mock.threeArgumentMethod(7, "", "test");
        control.setReturnValue(MockControl.ONE, "test");

        mock.threeArgumentMethod(8, null, "test2");
        control.setReturnValue(MockControl.ONE, "test2");

        final Object DEFAULT_VALUE = "Default object";
        control.setDefaultReturnValue(DEFAULT_VALUE);

        control.replay();
        assertEquals("test", mock.threeArgumentMethod(7, "", "test"));
        assertEquals("test2", mock.threeArgumentMethod(8, null, "test2"));
        assertSame("default Value",
            DEFAULT_VALUE,
            mock.threeArgumentMethod(7, new Object(), "test"));
		assertSame("default value 2",
			DEFAULT_VALUE,
			mock.threeArgumentMethod(7, "", "test"));
        assertSame(DEFAULT_VALUE, mock.threeArgumentMethod(8, null, "test"));
        assertSame(DEFAULT_VALUE, mock.threeArgumentMethod(9, null, "test"));

        control.verify();
    }

    public void testDefaultVoidCallable() {
        final RuntimeException EXPECTED = new RuntimeException("Expected");
        
        mock.twoArgumentMethod(1, 2);
        control.setDefaultVoidCallable();

        mock.twoArgumentMethod(1, 1);
        control.setThrowable(MockControl.ONE_OR_MORE, EXPECTED);

        control.replay();
        mock.twoArgumentMethod(2, 1);
        mock.twoArgumentMethod(1, 2);
        mock.twoArgumentMethod(3, 7);

        try {
            mock.twoArgumentMethod(1, 1);
            fail("Expected RuntimeException");
        } catch (RuntimeException actual) {
            assertSame("runtime exception", EXPECTED, actual);
        }
    }

     public void testDefaultThrowable() {
        mock.twoArgumentMethod(1, 2);
        control.setVoidCallable();
        mock.twoArgumentMethod(1, 1);
        control.setVoidCallable();

        RuntimeException expected = new RuntimeException();
        control.setDefaultThrowable(expected);

        control.replay();

        mock.twoArgumentMethod(1, 2);
        mock.twoArgumentMethod(1, 1);
        try {
            mock.twoArgumentMethod(2, 1);
            fail("RuntimeException expected");
        } catch (RuntimeException actual) {
            assertSame(expected, actual);
        }
    }
    
    public void testDefaultReturnValueBoolean() {
        mock.booleanReturningMethod(12);
        control.setReturnValue(true);
        control.setDefaultReturnValue(false);
        
        control.replay();

        assertFalse(mock.booleanReturningMethod(11));
		assertTrue(mock.booleanReturningMethod(12));
		assertFalse(mock.booleanReturningMethod(13));
    
		control.verify();
    }
}
