/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core;

import junit.framework.AssertionFailedError;

public class DynamicMockError 
	extends AssertionFailedError 
{
    public final DynamicMock dynamicMock;
    public final Invocation invocation;
    public final InvocationDispatcher dispatcher;
    
    public DynamicMockError( DynamicMock dynamicMock, 
							 Invocation invocation, 
							 InvocationDispatcher dispatcher, String message ) 
    {
        super(message);
        this.dynamicMock = dynamicMock;
        this.invocation = invocation;
        this.dispatcher = dispatcher;
    }
    
    public StringBuffer writeTo(StringBuffer buffer) {
        buffer.append(dynamicMock.toString()).append(": ")
        	.append(super.getMessage()).append("\n");
        buffer.append("Invoked: ");
        invocation.writeTo(buffer);
        buffer.append("in:\n");
        dispatcher.describeTo(buffer);
        return buffer;
    }
    
    public String getMessage() {
        return writeTo(new StringBuffer()).toString();
    }
}
