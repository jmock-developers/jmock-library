/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamock;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.dynamock.Mock;

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
    
    public void testPassesIfStubbedMethodCalled() {
    	actions.stubNoParams();
    	actions.callNoParams();
    	actions.verifyMock();
    }
    
    public void testPassesIfExpectedMethodCalled() {
        actions.expectNoParams();
        actions.callNoParams();
        actions.verifyMock();
    }

    public void testPassesIfStubbedMethodCalledTwice() {
    	actions.stubNoParams();
    	actions.callNoParams();
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
    
    public void testPassesIfStubbedMethodNotCalled() {
    	actions.stubNoParams();
    	actions.verifyMock();
    }
    
    public void testFailsIfExpectedMethodNotCalled() {
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
    
    public void testPassesIfStubbedMethodCalledWithParameters() {
    	actions.stubTwoParams();
    	actions.callTwoParams();
    	actions.verifyMock();
    }
    
    public void testPassesIfExpectedMethodCalledWithParameters() {
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
