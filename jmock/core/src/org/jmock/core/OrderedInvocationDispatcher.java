/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jmock.core.stub.TestFailureStub;

public class OrderedInvocationDispatcher implements InvocationDispatcher {
	public interface DispatchPolicy {
		public Object dispatch(List invokables, Invocation invocation, Stub defaultStub) throws Throwable;
	}
	
	public static final String NO_EXPECTATIONS_MESSAGE = "No expectations set";
	private DispatchPolicy policy;
	private List invokables = new ArrayList();
	private Stub defaultStub = new TestFailureStub("no match found");

	
	public OrderedInvocationDispatcher(DispatchPolicy policy) {
		this.policy = policy;
	}

	public Object dispatch(Invocation invocation) throws Throwable {
		return policy.dispatch(invokables, invocation, defaultStub);
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
	        ((Verifiable)i.next()).verify();
	    }
	}

	public void clear() {
	    invokables.clear();
	}

	public StringBuffer describeTo(StringBuffer buffer) {
	    if (anyInvokableHasDescription()) {
	        writeInvokablesTo(buffer);
	    } else {
	        buffer.append(NO_EXPECTATIONS_MESSAGE);
	    }
	
	    return buffer;
	}

	private void writeInvokablesTo(StringBuffer buffer) {
	    Iterator iterator = invokables.iterator();
	    while (iterator.hasNext()) {
	        Invokable invokable = (Invokable)iterator.next();
	        if (invokable.hasDescription()) {
	            invokable.describeTo(buffer).append("\n");
	        }
	    }
	}

	private boolean anyInvokableHasDescription() {
	    Iterator iterator = invokables.iterator();
	    while (iterator.hasNext()) {
	        if (((Invokable)iterator.next()).hasDescription()) return true;
	    }
	    return false;
	}

}
