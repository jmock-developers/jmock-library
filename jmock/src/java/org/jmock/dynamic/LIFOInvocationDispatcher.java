/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import org.jmock.expectation.Verifiable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class LIFOInvocationDispatcher implements InvocationDispatcher {

    private ArrayList invokables = new ArrayList();

    public Object dispatch(Invocation invocation) throws Throwable {
        ListIterator i = invokables.listIterator(invokables.size());
        while (i.hasPrevious()) {
            Invokable invokable = (Invokable) i.previous();
            if (invokable.matches(invocation)) {
                return invokable.invoke(invocation);
            }
        }
        throw new DynamicMockError(invocation, this, "No match found");
    }

    public void add(Invokable invokable) {
        invokables.add(invokable);
    }

    public void verify() {
        Iterator i = invokables.iterator();
        while (i.hasNext()) {
            ((Verifiable) i.next()).verify();
        }
    }

    public void clear() {
        invokables.clear();
    }


    public void writeTo(StringBuffer buffer) {
        Iterator iterator = invokables.iterator();
        while (iterator.hasNext()) {
            ((Invokable) iterator.next()).writeTo(buffer);
        }
    }
}
