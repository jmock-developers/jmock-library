/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import junit.framework.AssertionFailedError;

/**
 * @since 1.0
 */
public class DynamicMockError
        extends AssertionFailedError
{
    private static final long serialVersionUID = 1L;
    public final DynamicMock dynamicMock;
    public final Invocation invocation;
    public final InvocationDispatcher dispatcher;

    public DynamicMockError( DynamicMock dynamicMock,
                             Invocation invocation,
                             InvocationDispatcher dispatcher, String message ) {
        super(message);
        this.dynamicMock = dynamicMock;
        this.invocation = invocation;
        this.dispatcher = dispatcher;
    }

    public StringBuffer writeTo( StringBuffer buffer ) {
        buffer.append(dynamicMock.toString()).append(": ")
                .append(super.getMessage()).append("\n");
        buffer.append("Invoked: ");
        invocation.describeTo(buffer);
        buffer.append("\n");
        buffer.append("Allowed:\n");
        dispatcher.describeTo(buffer);
        return buffer;
    }

    public String getMessage() {
        return writeTo(new StringBuffer()).toString();
    }
}
