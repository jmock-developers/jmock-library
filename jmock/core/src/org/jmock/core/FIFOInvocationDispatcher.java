/*
 * Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.List;
import java.util.ListIterator;

public class FIFOInvocationDispatcher 
	extends OrderedInvocationDispatcher 
{
	static private class FIFOPolicy 
		implements DispatchPolicy 
	{
		public Object dispatch(List invokables, Invocation invocation,
				Stub defaultStub) throws Throwable 
		{
			ListIterator i = invokables.listIterator();
			while (i.hasNext()) {
				Invokable invokable = (Invokable) i.next();
				if (invokable.matches(invocation)) {
					return invokable.invoke(invocation);
				}
			}

			return defaultStub.invoke(invocation);
		}
	}

	public FIFOInvocationDispatcher() {
		super(new FIFOPolicy());
	}
}