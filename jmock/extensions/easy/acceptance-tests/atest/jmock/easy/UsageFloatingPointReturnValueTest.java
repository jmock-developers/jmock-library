/*
 * Copyright (c) 2001-2004 OFFIS. This program is made available under the terms of
 * the MIT License.
 * Copyright (c) 2004 jmock.org
 */
package atest.jmock.easy;

import junit.framework.TestCase;

import org.jmock.easy.MockControl;

public class UsageFloatingPointReturnValueTest extends TestCase {
    MockControl control;
    IMethods mock;

    public UsageFloatingPointReturnValueTest(String name) {
        super(name);
    }

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testReturnFloat() {
        mock.floatReturningMethod(0);
        control.setReturnValue(25.0F);
        control.setDefaultReturnValue(34.0F);

        control.replay();

        assertEquals(25.0F, mock.floatReturningMethod(0), 0.0F);
        assertEquals(34.0F, mock.floatReturningMethod(-4), 0.0F);
        assertEquals(34.0F, mock.floatReturningMethod(12), 0.0F);

        control.verify();
    }

    public void testReturnDouble() {
        mock.doubleReturningMethod(0);
        control.setReturnValue(25.0);
        control.setDefaultReturnValue(34.0);

        control.replay();

        assertEquals(25.0, mock.doubleReturningMethod(0), 0.0);
        assertEquals(34.0, mock.doubleReturningMethod(-4), 0.0);
        assertEquals(34.0, mock.doubleReturningMethod(12), 0.0);

        control.verify();
    }
}
