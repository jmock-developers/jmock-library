/*
 * Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.*;

import org.jmock.core.stub.TestFailureStub;

public class OrderedInvocationDispatcher implements InvocationDispatcher {
	public interface InvokablesCollection {
		InvokablesIterator iterator();
		void add(Invokable invokable);
		void clear();
	}
	public interface DispatchPolicy {
		InvokablesCollection makeCollection();
	}

	public interface InvokablesIterator {
		boolean hasMore();
		Invokable next();
	}

	public static final String NO_EXPECTATIONS_MESSAGE = "No expectations set";
	private InvokablesCollection invokables;
	private Stub defaultStub = new TestFailureStub("no match found");

	public OrderedInvocationDispatcher(InvokablesCollection invokables) {
		this.invokables = invokables;
	}

	public Object dispatch(Invocation invocation) throws Throwable {
		InvokablesIterator i = invokables.iterator();
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
		InvokablesIterator i = invokables.iterator();
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

	private void writeInvokablesTo(StringBuffer buffer) {
		InvokablesIterator iterator = invokables.iterator();
		while (iterator.hasMore()) {
			Invokable invokable = iterator.next();
			if (invokable.hasDescription()) {
				invokable.describeTo(buffer).append("\n");
			}
		}
	}

	private boolean anyInvokableHasDescription() {
		InvokablesIterator iterator = invokables.iterator();
		while (iterator.hasMore()) {
			if (iterator.next().hasDescription()) {
				return true;
			}
		}
		return false;
	}

	static public class FIFOInvokablesCollection implements InvokablesCollection {
		private List list = new ArrayList();

		public InvokablesIterator iterator() {
			return new InvokablesIterator() {
				private Iterator iterator = list.iterator();

				public boolean hasMore() {
					return iterator.hasNext();
				}
				public Invokable next() {
					return (Invokable) iterator.next();
				}
			};
		}

		public void add(Invokable invokable) {
			list.add(invokable);
		}
		public void clear() {
			list.clear();
		}
	}

	static public class LIFOInvokablesCollection implements InvokablesCollection {
		private List list = new ArrayList();

		public InvokablesIterator iterator() {
			return new InvokablesIterator() {
				ListIterator i = list.listIterator(list.size());

				public boolean hasMore() {
					return i.hasPrevious();
				}
				public Invokable next() {
					return (Invokable) i.previous();
				}
			};
		}

		public void add(Invokable invokable) {
			list.add(invokable);
		}
		public void clear() {
			list.clear();
		}
	}
}