/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import junit.framework.AssertionFailedError;

public class DynamicMockError 
	extends AssertionFailedError 
{
    public final Invocation invocation;
    public final InvocationDispatcher dispatcher;

    public DynamicMockError( Invocation invocation, 
							 InvocationDispatcher dispatcher, 
							 String message ) 
    {
        super(message);
        this.invocation = invocation;
        this.dispatcher = dispatcher;
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        buffer.append(super.getMessage()).append("\n");
        invocation.writeTo(buffer);
        buffer.append("in:\n");
        dispatcher.writeTo(buffer);
        return buffer;
    }

    public String getMessage() {
        return writeTo(new StringBuffer()).toString();
    }
}
