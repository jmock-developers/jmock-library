/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.functional;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.Mock;

/**
 * High level test of dynamic mock class.
 */
public abstract class AbstractMockTest extends TestCase {
    private MockTestActions actions;

    public abstract MockTestActions createActions();

    public void setUp() {
        actions = createActions();
    }

    public void testHasDefaultNameBasedOnMockedType() {
        Mock mock = new Mock(MockTestActions.class);
        assertEquals("Should have same name", "mockMockTestActions", mock.toString());
    }

    public void testCanBeExplicitlyNamed() {
        Mock otherMock = new Mock(MockTestActions.class, "otherMock");
        assertEquals("Should have same name", "otherMock", otherMock.toString());
    }

    public void testPassesIfMockedMethodCalled() {
        actions.expectNoParams();
        actions.callNoParams();
        actions.verifyMock();
    }

    public void testFailsIfMockedMethodCalledTwice() {
        actions.expectNoParams();
        actions.callNoParams();
        try {
            actions.callNoParams();
            fail("Should have throw exception");
        } catch (AssertionFailedError expected) {
            return;
        }
    }

    public void testFailsIfMockedMethodNotCalled() {
        actions.expectNoParams();

        try {
            actions.verifyMock();
        } catch (AssertionFailedError unused) {
            return;
        }
        fail("Should have thrown exception");
    }

    public void testFailsImmediatelyIfUnexpectedMethodCalled() {
        actions.expectNotNoParams();

        try {
            actions.callNoParams();
        } catch (AssertionFailedError unused) {
            return;
        }
        fail("Should have thrown exception");
    }

    public void testPassesIfMockedMethodCalledWithParameters() {
        actions.expectTwoParams();
        actions.callTwoParams();
        actions.verifyMock();
    }

    public void testInvocationFailsIfParameterValueIncorrect() {
        actions.expectTwoParams();

        try {
            actions.callIncorrectSecondParameter();
        } catch (AssertionFailedError unused) {
            return;
        }
        fail("Should have thrown exception");
    }

}
