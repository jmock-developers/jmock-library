/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.jmock.Verifiable;


public class LIFOInvocationDispatcher 
    implements InvocationDispatcher 
{
    public static final String NO_EXPECTATIONS_MESSAGE = "No expectations set";
    
	private ArrayList invokables = new ArrayList();
    private Stub defaultStub = new NoMatchFoundStub(this);

    public Object dispatch(Invocation invocation) throws Throwable {
        ListIterator i = invokables.listIterator(invokables.size());
        while (i.hasPrevious()) {
            Invokable invokable = (Invokable) i.previous();
            if (invokable.matches(invocation)) {
                return invokable.invoke(invocation);
            }
        }
        
        return defaultStub.invoke(invocation);
    }
    
    public void setDefaultStub(Stub defaultStub) {
        this.defaultStub = defaultStub;
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
        int oldLength = buffer.length();
        writeInvokablesTo( buffer );
        
        if (buffer.length() == oldLength ) {
        	buffer.append(NO_EXPECTATIONS_MESSAGE);
        }
    }

	private void writeInvokablesTo(StringBuffer buffer) {
		Iterator iterator = invokables.iterator();
		while (iterator.hasNext()) {
		    ((Invokable) iterator.next()).writeTo(buffer);
		}
	}
}
