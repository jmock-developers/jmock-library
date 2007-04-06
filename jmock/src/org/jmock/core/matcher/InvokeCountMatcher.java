package org.jmock.core.matcher;

import org.jmock.core.Invocation;


public class InvokeCountMatcher extends InvokedRecorder
{
    private int expectedCount;

    public InvokeCountMatcher( int expectedCount ) {
        this.expectedCount = expectedCount;
    }

    public boolean matches( Invocation invocation ) {
        return getInvocationCount() < expectedCount;
    }

    public void verify() {
        verifyHasBeenInvokedExactly(expectedCount);
    }

    public boolean hasDescription() {
        return true;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("expected ")
                     .append(expectedCount)
                     .append(" times, invoked ")
                     .append(getInvocationCount())
                     .append(" times");
    }
}
