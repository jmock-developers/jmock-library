/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.List;
import java.util.ListIterator;


public class LIFOInvocationDispatcher
        extends OrderedInvocationDispatcher
{
	static private class LIFOPolicy
		implements DispatchPolicy
	{
		public Object dispatch(List invokables, Invocation invocation,
				Stub defaultStub) throws Throwable
		{
	        ListIterator i = invokables.listIterator(invokables.size());
	        while (i.hasPrevious()) {
	            Invokable invokable = (Invokable)i.previous();
	            if (invokable.matches(invocation)) {
	                return invokable.invoke(invocation);
	            }
	        }
	        return defaultStub.invoke(invocation);
		}
	}
	
	public LIFOInvocationDispatcher() {
		super(new LIFOPolicy());
	}
}
