/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.matcher;

import junit.framework.Assert;
import org.jmock.core.Invocation;
import org.jmock.core.InvocationMatcher;


public class InvokedRecorder
        implements InvocationMatcher
{
    private int invocationCount = 0;

    public int getInvocationCount() {
        return invocationCount;
    }

    public boolean hasBeenInvoked() {
        return invocationCount > 0;
    }

    public boolean matches( Invocation invocation ) {
        return true;
    }

    public void invoked( Invocation invocation ) {
        invocationCount++;
    }

    public boolean hasDescription() {
        return false;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer;
    }

    public void verify() {
        // always verifies
    }

    public void verifyHasBeenInvoked() {
        Assert.assertTrue("expected method was not invoked", hasBeenInvoked() );
    }

    public void verifyHasBeenInvokedExactly( int expectedCount ) {
        Assert.assertTrue("expected method was not invoked the expected number of times: expected " +
                          expectedCount + " times, was invoked " + invocationCount + " times",
                          invocationCount == expectedCount );
    }
}
