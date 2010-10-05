/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.matcher;


public class InvokeAtLeastOnceMatcher
        extends InvokedRecorder
{
    public void verify() {
        verifyHasBeenInvoked();
    }

    public boolean hasDescription() {
        return true;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        buffer.append("expected at least once");
        if (hasBeenInvoked()) {
            buffer.append(" and has been invoked");
        }
        return buffer;
    }
}
