/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import junit.framework.AssertionFailedError;

public class DynamicMockError 
	extends AssertionFailedError 
{
    public final CoreMock coreMock;
    public final Invocation invocation;
    public final InvocationDispatcher dispatcher;

    public DynamicMockError( CoreMock coreMock, 
							 Invocation invocation, 
							 InvocationDispatcher dispatcher, String message ) 
    {
        super(message);
        this.coreMock = coreMock;
        this.invocation = invocation;
        this.dispatcher = dispatcher;
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        buffer.append(coreMock.getMockName()).append(": ")
        	.append(super.getMessage()).append("\n");
        buffer.append("Invoked: ");
        invocation.writeTo(buffer);
        buffer.append("in:\n");
        dispatcher.writeTo(buffer);
        return buffer;
    }

    public String getMessage() {
        return writeTo(new StringBuffer()).toString();
    }
}
