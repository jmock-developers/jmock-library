/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;

import junit.framework.Assert;


public class ExpectationCounter extends AbstractExpectation
{
    private int myExpectedCalls = 0;
    private int myActualCalls = 0;

    public ExpectationCounter( String name ) {
        super(name);
    }

    public void clearActual() {
        myActualCalls = 0;
    }

    public void inc() {
        myActualCalls++;
        if (shouldCheckImmediately()) {
            Assert.assertTrue(myName + " should not be called more than " + myExpectedCalls + " times",
                              myActualCalls <= myExpectedCalls);
        }
    }

    public void setExpected( int expectedCalls ) {
        myExpectedCalls = expectedCalls;
        setHasExpectations();
    }

    public void setExpectNothing() {
        myExpectedCalls = 0;
        setHasExpectations();
    }

    public void verify() {
        assertEquals("did not receive the expected Count.",
                     myExpectedCalls,
                     myActualCalls);
    }
}
