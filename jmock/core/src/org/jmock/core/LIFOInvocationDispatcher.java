/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.ListIterator;


public class LIFOInvocationDispatcher
        extends OrderedInvocationDispatcher
{
    public Object dispatch( Invocation invocation ) throws Throwable {
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
