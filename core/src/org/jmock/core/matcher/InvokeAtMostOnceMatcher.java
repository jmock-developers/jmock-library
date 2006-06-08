/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.matcher;

import org.jmock.core.Invocation;

/**
 * @since 1.1.0
 */
public class InvokeAtMostOnceMatcher
        extends InvokedRecorder
{
    public boolean matches( Invocation invocation ) {
        return !hasBeenInvoked();
    }

    public boolean hasDescription() {
        return true;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        buffer.append("expected at most once");
        if (hasBeenInvoked()) buffer.append(" and has been invoked");
        return buffer;
    }
}
