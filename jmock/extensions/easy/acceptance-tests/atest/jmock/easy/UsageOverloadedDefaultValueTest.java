/*
 * Copyright (c) 2001-2004 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package atest.jmock.easy;

import junit.framework.TestCase;

import org.jmock.easy.MockControl;

public class UsageOverloadedDefaultValueTest extends TestCase {
    MockControl control;
    IMethods mock;

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testOverloading() {

        mock.oneArgumentMethod(true);
        control.setReturnValue("true");
        control.setDefaultReturnValue("false");

        mock.oneArgumentMethod((byte) 0);
        control.setReturnValue("byte 0");
        control.setDefaultReturnValue("byte 1");

        mock.oneArgumentMethod((short) 0);
        control.setReturnValue("short 0");
        control.setDefaultReturnValue("short 1");

        mock.oneArgumentMethod((char) 0);
        control.setReturnValue("char 0");
        control.setDefaultReturnValue("char 1");

        mock.oneArgumentMethod(0);
        control.setReturnValue("int 0");
        control.setDefaultReturnValue("int 1");

        mock.oneArgumentMethod((long) 0);
        control.setReturnValue("long 0");
        control.setDefaultReturnValue("long 1");

        mock.oneArgumentMethod((float) 0);
        control.setReturnValue("float 0");
        control.setDefaultReturnValue("float 1");

        mock.oneArgumentMethod(0.0);
        control.setReturnValue("double 0");
        control.setDefaultReturnValue("double 1");

        mock.oneArgumentMethod("Object 0");
        control.setReturnValue("String 0");
        control.setDefaultReturnValue("String 1");

        control.replay();

        assertEquals("true", mock.oneArgumentMethod(true));
        assertEquals("false", mock.oneArgumentMethod(false));

        assertEquals("byte 0", mock.oneArgumentMethod((byte) 0));
        assertEquals("byte 1", mock.oneArgumentMethod((byte) 1));

        assertEquals("short 0", mock.oneArgumentMethod((short) 0));
        assertEquals("short 1", mock.oneArgumentMethod((short) 1));

        assertEquals("char 0", mock.oneArgumentMethod((char) 0));
        assertEquals("char 1", mock.oneArgumentMethod((char) 1));

        assertEquals("int 0", mock.oneArgumentMethod(0));
        assertEquals("int 1", mock.oneArgumentMethod(1));

        assertEquals("long 0", mock.oneArgumentMethod((long) 0));
        assertEquals("long 1", mock.oneArgumentMethod((long) 1));

        assertEquals("float 0", mock.oneArgumentMethod((float) 0.0));
        assertEquals("float 1", mock.oneArgumentMethod((float) 1.0));

        assertEquals("double 0", mock.oneArgumentMethod(0.0));
        assertEquals("double 1", mock.oneArgumentMethod(1.0));

        assertEquals("String 0", mock.oneArgumentMethod("Object 0"));
        assertEquals("String 1", mock.oneArgumentMethod("Object 1"));

        control.verify();
    }

    public void testDefaultThrowable() {

        mock.oneArgumentMethod("Object");
        RuntimeException expected = new RuntimeException();
        control.setDefaultThrowable(expected);

        control.replay();

        try {
            mock.oneArgumentMethod("Something else");
            fail("runtime exception expected");
        } catch (RuntimeException expectedException) {
            assertSame(expected, expectedException);
        }
    }
}