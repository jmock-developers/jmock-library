/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.jmock.Verifiable;
import org.jmock.dynamic.stub.CustomStub;


public class LIFOInvocationDispatcher 
    implements InvocationDispatcher 
{
    private ArrayList invokables = new ArrayList();
    private Stub defaultStub = new CustomStub("report no matching method") {
    	public Object invoke( Invocation invocation ) throws Throwable {
    		throw new DynamicMockError( invocation, LIFOInvocationDispatcher.this, 
    				                    "No match found" );
        }
    };

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
    
    public Stub getDefaultStub() {
        return defaultStub;
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
        Iterator iterator = invokables.iterator();
        while (iterator.hasNext()) {
            ((Invokable) iterator.next()).writeTo(buffer);
        }
    }
}
