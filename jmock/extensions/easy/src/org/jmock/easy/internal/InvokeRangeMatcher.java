package org.jmock.easy.internal;

import junit.framework.Assert;

import org.jmock.core.Invocation;
import org.jmock.core.matcher.InvokedRecorder;


public class InvokeRangeMatcher extends InvokedRecorder
{
    private Range range;

    public InvokeRangeMatcher( Range range ) {
        this.range = range;
    }

    public boolean matches( Invocation invocation ) {
        return range.contains(getInvocationCount());
    }

    public void verify() {
        Assert.assertTrue(
        		"method was not invoked the expected number of times: "
        		+ range.expectedAndActual(getInvocationCount()),
				range.contains(getInvocationCount()));
    }

    public boolean hasDescription() {
        return true;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append(range.expectedAndActual(getInvocationCount()));
    }
}
