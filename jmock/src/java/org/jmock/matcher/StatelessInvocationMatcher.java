/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.matcher;

import org.jmock.InvocationMatcher;
import org.jmock.dynamic.Invocation;

public abstract class StatelessInvocationMatcher implements InvocationMatcher {
    public void invoked(Invocation invocation) {
        // Do nothing because state cannot change
    }

    public void verify() {
        // Nothing to verify because state cannot change
    }
}