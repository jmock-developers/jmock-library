/*
 * Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jmock.core.stub.TestFailureStub;

public class OrderedInvocationDispatcher implements InvocationDispatcher {
	public interface DispatchPolicy {
		InvokableIterator dispatchIterator(List invokables);
	}

	public interface InvokableIterator {
		boolean hasMore();
		Invokable next();
	}

	public static final String NO_EXPECTATIONS_MESSAGE = "No expectations set";
	private DispatchPolicy policy;
	private List invokables = new ArrayList();
	private Stub defaultStub = new TestFailureStub("no match found");

	public OrderedInvocationDispatcher(DispatchPolicy policy) {
		this.policy = policy;
	}

	public Object dispatch(Invocation invocation) throws Throwable {
		InvokableIterator i = policy.dispatchIterator(invokables);
		while (i.hasMore()) {
			Invokable invokable = i.next();
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
			Invokable invokable = (Invokable) iterator.next();
			if (invokable.hasDescription()) {
				invokable.describeTo(buffer).append("\n");
			}
		}
	}

	private boolean anyInvokableHasDescription() {
		Iterator iterator = invokables.iterator();
		while (iterator.hasNext()) {
			if (((Invokable) iterator.next()).hasDescription())
				return true;
		}
		return false;
	}

	static public class FIFO extends OrderedInvocationDispatcher {
		public FIFO() { super(POLICY); }
		private static final DispatchPolicy POLICY = new DispatchPolicy() {
			public InvokableIterator dispatchIterator(final List invokables) {
				return new InvokableIterator() {
					private Iterator iterator = invokables.iterator();
                    
					public boolean hasMore() { return iterator.hasNext(); }
					public Invokable next() { return (Invokable) iterator.next(); }
				};
			}
		};
	}

	static public class LIFO extends OrderedInvocationDispatcher {
		public LIFO() { super(POLICY); }
		private static final DispatchPolicy POLICY = new DispatchPolicy() {
			public InvokableIterator dispatchIterator(final List invokables) {
				return new InvokableIterator() {
					ListIterator i = invokables.listIterator(invokables.size());
                    
					public boolean hasMore() { return i.hasPrevious(); }
					public Invokable next() { return (Invokable) i.previous(); }
				};
			}
		};
	}
}