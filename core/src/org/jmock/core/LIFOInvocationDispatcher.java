/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.jmock.core.stub.TestFailureStub;


public class LIFOInvocationDispatcher 
    implements InvocationDispatcher 
{
    public static final String NO_EXPECTATIONS_MESSAGE = "No expectations set";
    
	private ArrayList invokables = new ArrayList();
    private Stub defaultStub = new TestFailureStub("no match found");
    
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
    
    public StringBuffer describeTo(StringBuffer buffer) {
        if( anyInvokableHasDescription() ) {
            writeInvokablesTo( buffer );
        } else {
        	buffer.append(NO_EXPECTATIONS_MESSAGE);
        }
        
        return buffer;
    }
    
	private void writeInvokablesTo(StringBuffer buffer) {
		Iterator iterator = invokables.iterator();
		while (iterator.hasNext()) {
		    Invokable invokable = (Invokable) iterator.next();
            if( invokable.hasDescription() ) {
                invokable.describeTo(buffer).append("\n");
            }
		}
	}
    
    private boolean anyInvokableHasDescription() {
        Iterator iterator = invokables.iterator();
        while (iterator.hasNext()) {
            if (((Invokable) iterator.next()).hasDescription()) return true;
        }
        return false;
    }
}
