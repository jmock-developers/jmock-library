/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.matcher;

import junit.framework.Assert;
import org.jmock.InvocationMatcher;
import org.jmock.dynamic.Invocation;

public class CallOnceMatcher implements InvocationMatcher {
    private boolean wasInvoked = false;

    public boolean matches(Invocation invocation) {
        return !wasInvoked;
    }

    public void invoked(Invocation invocation) {
        wasInvoked = true;
    }

    public void verify() {
        Assert.assertTrue("Should have been called", wasInvoked);
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        return buffer.append("Call once");
    }
}
