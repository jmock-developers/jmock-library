/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.matcher;

import org.jmock.core.Invocation;


public class InvokeOnceMatcher
        extends InvokedRecorder
{
    public boolean matches( Invocation invocation ) {
        return !hasBeenInvoked();
    }

    public void verify() {
        verifyHasBeenInvoked();
    }

    public boolean hasDescription() {
        return true;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        buffer.append("expected once");
        if (hasBeenInvoked()) buffer.append(" and has been invoked");
        return buffer;
    }
}
