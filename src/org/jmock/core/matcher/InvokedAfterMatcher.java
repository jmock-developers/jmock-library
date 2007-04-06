/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.matcher;

import junit.framework.AssertionFailedError;
import org.jmock.core.Invocation;


public class InvokedAfterMatcher
        extends StatelessInvocationMatcher
{
    private InvokedRecorder priorCallRecorder;
    private String priorCallDescription;

    public InvokedAfterMatcher( InvokedRecorder priorCallRecorder,
                                String priorCallDescription ) {
        this.priorCallRecorder = priorCallRecorder;
        this.priorCallDescription = priorCallDescription;
    }

    public boolean matches( Invocation invocation ) {
        return priorCallRecorder.hasBeenInvoked();
    }

    public void invoked( Invocation invocation ) {
        if (!matches(invocation)) {
            throw new AssertionFailedError("called out of order; should be called after " + priorCallDescription);
        }
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        buffer.append("after ").append(priorCallDescription);
        if( priorCallRecorder.hasBeenInvoked() ) {
            buffer.append(" (invoked)");
        } else {
            buffer.append(" (not invoked)");
        }
        return buffer;
    }
}
