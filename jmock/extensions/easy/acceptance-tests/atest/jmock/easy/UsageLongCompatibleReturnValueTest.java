/*
 * Copyright (c) 2001-2004 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package atest.jmock.easy;

import junit.framework.TestCase;

import org.jmock.easy.MockControl;

public class UsageLongCompatibleReturnValueTest extends TestCase {
    MockControl control;
    IMethods mock;

    public UsageLongCompatibleReturnValueTest(String name) {
        super(name);
    }

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testReturnByte() {
        mock.byteReturningMethod(0);
        control.setReturnValue(25);
        control.setDefaultReturnValue(34);

        control.replay();

        assertEquals(25, mock.byteReturningMethod(0));
        assertEquals(34, mock.byteReturningMethod(-4));
        assertEquals(34, mock.byteReturningMethod(12));

        control.verify();
    }

    public void testReturnShort() {
        mock.shortReturningMethod(0);
        control.setReturnValue(25);
        control.setDefaultReturnValue(34);

        control.replay();

        assertEquals(25, mock.shortReturningMethod(0));
        assertEquals(34, mock.shortReturningMethod(-4));
        assertEquals(34, mock.shortReturningMethod(12));

        control.verify();
    }

    public void testReturnChar() {
        mock.charReturningMethod(0);
        control.setReturnValue(25);
        control.setDefaultReturnValue(34);

        control.replay();

        assertEquals(25, mock.charReturningMethod(0));
        assertEquals(34, mock.charReturningMethod(-4));
        assertEquals(34, mock.charReturningMethod(12));

        control.verify();
    }

    public void testReturnInt() {
        mock.intReturningMethod(0);
        control.setReturnValue(25);
        control.setDefaultReturnValue(34);

        control.replay();

        assertEquals(25, mock.intReturningMethod(0));
        assertEquals(34, mock.intReturningMethod(-4));
        assertEquals(34, mock.intReturningMethod(12));

        control.verify();
    }

    public void testReturnLong() {
        mock.longReturningMethod(0);
        control.setReturnValue(25);
        control.setDefaultReturnValue(34);

        control.replay();

        assertEquals(25, mock.longReturningMethod(0));
        assertEquals(34, mock.longReturningMethod(-4));
        assertEquals(34, mock.longReturningMethod(12));

        control.verify();
    }
}
