/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.matcher;

import junit.framework.AssertionFailedError;
import org.jmock.core.Invocation;
import org.jmock.core.InvocationMatcher;


public class TestFailureMatcher
        implements InvocationMatcher
{
    private String errorMessage;

    public TestFailureMatcher( String errorMessage ) {
        this.errorMessage = errorMessage;
    }

    public boolean matches( Invocation invocation ) {
        return true;
    }

    public boolean hasDescription() {
        return true;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append(errorMessage);
    }

    public void invoked( Invocation invocation ) {
        throw new AssertionFailedError(errorMessage);
    }

    public void verify() {
        // Always verify
    }
}
