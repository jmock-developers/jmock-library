/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.ListIterator;


public class FIFOInvocationDispatcher
        extends OrderedInvocationDispatcher
{
    public Object dispatch( Invocation invocation ) throws Throwable {
        ListIterator i = invokables.listIterator();
        while (i.hasNext()) {
            Invokable invokable = (Invokable)i.next();
            if (invokable.matches(invocation)) {
                return invokable.invoke(invocation);
            }
        }

        return defaultStub.invoke(invocation);
    }
}
