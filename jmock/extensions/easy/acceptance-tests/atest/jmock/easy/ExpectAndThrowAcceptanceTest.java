/*
 * Copyright (c) 2001-2004 OFFIS. This program is made available under the terms of
 * the MIT License.
 */
package atest.jmock.easy;

import junit.framework.TestCase;

import org.jmock.easy.MockControl;

public class ExpectAndThrowAcceptanceTest extends TestCase {
    private MockControl control;
    private IMethods mock;
    private static RuntimeException EXCEPTION = new RuntimeException();

    protected void setUp() {
        control = MockControl.createControl(IMethods.class);
        mock = (IMethods) control.getMock();
    }

    public void testBoolean() {
        control.expectAndThrow(mock.booleanReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.booleanReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testLong() {
        control.expectAndThrow(mock.longReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.longReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testFloat() {
        control.expectAndThrow(mock.floatReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.floatReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testDouble() {
        control.expectAndThrow(mock.doubleReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.doubleReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testObject() {
        control.expectAndThrow(mock.objectReturningMethod(4), EXCEPTION);
        control.replay();
        try {
            mock.objectReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testBooleanAndRange() {
        control.expectAndThrow(
            mock.booleanReturningMethod(4),
            EXCEPTION,
            MockControl.ONE);
        control.replay();
        try {
            mock.booleanReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testLongAndRange() {
        control.expectAndThrow(
            mock.longReturningMethod(4),
            EXCEPTION,
            MockControl.ONE);
        control.replay();
        try {
            mock.longReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testFloatAndRange() {
        control.expectAndThrow(
            mock.floatReturningMethod(4),
            EXCEPTION,
            MockControl.ONE);
        control.replay();
        try {
            mock.floatReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testDoubleAndRange() {
        control.expectAndThrow(
            mock.doubleReturningMethod(4),
            EXCEPTION,
            MockControl.ONE);
        control.replay();
        try {
            mock.doubleReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testObjectAndRange() {
        control.expectAndThrow(
            mock.objectReturningMethod(4),
            EXCEPTION,
            MockControl.ONE);
        control.replay();
        try {
            mock.objectReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testBooleanAndCount() {
        control.expectAndThrow(mock.booleanReturningMethod(4), EXCEPTION, 2);
        control.replay();
        try {
            mock.booleanReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.booleanReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testLongAndCount() {
        control.expectAndThrow(mock.longReturningMethod(4), EXCEPTION, 2);
        control.replay();
        try {
            mock.longReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.longReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testFloatAndCount() {
        control.expectAndThrow(mock.floatReturningMethod(4), EXCEPTION, 2);
        control.replay();
        try {
            mock.floatReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.floatReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testDoubleAndCount() {
        control.expectAndThrow(mock.doubleReturningMethod(4), EXCEPTION, 2);
        control.replay();
        try {
            mock.doubleReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.doubleReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testObjectAndCount() {
        control.expectAndThrow(mock.objectReturningMethod(4), EXCEPTION, 2);
        control.replay();
        try {
            mock.objectReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.objectReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testBooleanAndMinMax() {
        control.expectAndThrow(mock.booleanReturningMethod(4), EXCEPTION, 2, 3);
        control.replay();
        try {
            mock.booleanReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.booleanReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
        try {
            mock.booleanReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testLongAndMinMax() {
        control.expectAndThrow(mock.longReturningMethod(4), EXCEPTION, 2, 3);
        control.replay();
        try {
            mock.longReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.longReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
        try {
            mock.longReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testFloatAndMinMax() {
        control.expectAndThrow(mock.floatReturningMethod(4), EXCEPTION, 2, 3);
        control.replay();
        try {
            mock.floatReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.floatReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
        try {
            mock.floatReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testDoubleAndMinMax() {
        control.expectAndThrow(mock.doubleReturningMethod(4), EXCEPTION, 2, 3);
        control.replay();
        try {
            mock.doubleReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.doubleReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
        try {
            mock.doubleReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

    public void testObjectAndMinMax() {
        control.expectAndThrow(mock.objectReturningMethod(4), EXCEPTION, 2, 3);
        control.replay();
        try {
            mock.objectReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        try {
            mock.objectReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
        try {
            mock.objectReturningMethod(4);
        } catch (RuntimeException exception) {
            assertSame(EXCEPTION, exception);
        }
        control.verify();
    }

}
