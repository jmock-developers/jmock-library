/*
 * Copyright (c) 2001-2004 OFFIS. This program is made available under the terms of
 * the MIT License.
 * Copyright (c) 2004 jmock.org
 */
package atest.jmock.easy;

import org.jmock.easy.MockControl;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class CallCountAcceptanceTest extends TestCase {

    private MockControl control;
    private VoidMethodInterface mock;

    private interface VoidMethodInterface {
        void method();
    }

    protected void setUp() {
        control = MockControl.createControl(VoidMethodInterface.class);
        mock = (VoidMethodInterface) control.getMock();
    }
    public void testMockWithNoExpectedCallsPassesWithNoCalls() {
        control.replay();
        control.verify();
    }
    public void testMockWithNoExpectedCallsFailsAtFirstCall() {
        control.replay();
        assertMethodCallFails();
    }

    public void testMockWithOneExpectedCallFailsAtVerify() {
        callMethodOnce();
        control.replay();
        assertVerifyFails();
    }

    public void testMockWithOneExpectedCallPassesWithOneCall() {
        callMethodOnce();
        control.replay();
        callMethodOnce();
        control.verify();
    }

    public void testMockWithOneExpectedCallFailsAtSecondCall() {
        callMethodOnce();
        control.replay();
        callMethodOnce();
        assertMethodCallFails();
    }

    public void testTooFewCalls() {
        callMethodThreeTimes();
        control.replay();
        callMethodTwice();
        assertVerifyFails();
    }

    public void testCorrectNumberOfCalls() {
        callMethodThreeTimes();
        control.replay();
        callMethodThreeTimes();
        control.verify();
    }

    public void testTooManyCalls() {
        callMethodThreeTimes();
        control.replay();
        callMethodThreeTimes();
        assertMethodCallFails();
    }

    public void xtestNoUpperLimitWithoutCallCountSet() {
        mock.method();
        // TODO control.setVoidCallable(MockControl.ONE_OR_MORE); 
        control.replay();
        assertVerifyFails();
        mock.method();
        control.verify();
        mock.method();
        control.verify();
        mock.method();
        control.verify();
    }

    private void callMethodOnce() {
        mock.method();
    }

    private void callMethodTwice() {
        mock.method();
        mock.method();
    }

    private void callMethodThreeTimes() {
        mock.method();
        mock.method();
        mock.method();
    }

    private void assertVerifyFails() {
        try {
            control.verify();
            fail("Expected AssertionFailedError");
        } catch (AssertionFailedError expected) {
        }
    }

    private void assertMethodCallFails() {
        try {
            mock.method();
            fail("Expected AssertionFailedError");
        } catch (AssertionFailedError expected) {
        }
    }

}
