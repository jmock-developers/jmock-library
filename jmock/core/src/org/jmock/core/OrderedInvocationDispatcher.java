/*
 * Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.ArrayList;
import java.util.ListIterator;

import org.jmock.core.internal.HiddenInvocationMocker;
import org.jmock.core.stub.TestFailureStub;

public abstract class OrderedInvocationDispatcher implements InvocationDispatcher {
	public static final String UNEXPECTED_INVOCATION_MESSAGE = "unexpected invocation";
	public static final String NO_EXPECTATIONS_MESSAGE = "No expectations set";
    
	protected ArrayList invokables = new ArrayList();
	private Stub defaultStub = new TestFailureStub(UNEXPECTED_INVOCATION_MESSAGE);

    abstract public InvokablesIterator iterator();
    
	public Object dispatch(Invocation invocation) throws Throwable {
		InvokablesIterator i = iterator();
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
		InvokablesIterator i = iterator();
		while (i.hasMore()) {
			i.next().verify();
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

    public void setupDefaultBehaviour(String name, Object proxy) {
        add(new HiddenInvocationMocker.ToString(name));
        add(new HiddenInvocationMocker.Equals(proxy));
        add(new HiddenInvocationMocker.HashCode());
    }

	private void writeInvokablesTo(StringBuffer buffer) {
		InvokablesIterator iterator = iterator();
		while (iterator.hasMore()) {
			Invokable invokable = iterator.next();
			if (invokable.hasDescription()) {
				invokable.describeTo(buffer).append("\n");
			}
		}
	}

	private boolean anyInvokableHasDescription() {
		InvokablesIterator iterator = iterator();
		while (iterator.hasMore()) {
			if (iterator.next().hasDescription()) {
				return true;
			}
		}
		return false;
	}


    // ----------------------------------------------------------
	static public class FIFO extends OrderedInvocationDispatcher {
		public InvokablesIterator iterator() {
			return new InvokablesIterator() {
				private java.util.Iterator iterator = invokables.iterator();

				public boolean hasMore() {
					return iterator.hasNext();
				}
				public Invokable next() {
					return (Invokable) iterator.next();
				}
			};
		}
	}

	static public class LIFO extends OrderedInvocationDispatcher {
		public InvokablesIterator iterator() {
			return new InvokablesIterator() {
				ListIterator i = invokables.listIterator(invokables.size());

				public boolean hasMore() {
					return i.hasPrevious();
				}
				public Invokable next() {
					return (Invokable) i.previous();
				}
			};
		}
	}

}