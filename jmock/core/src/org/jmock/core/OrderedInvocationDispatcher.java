/*
 * Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jmock.core.constraint.IsAnything;
import org.jmock.core.matcher.ArgumentsMatcher;
import org.jmock.core.matcher.MethodNameMatcher;
import org.jmock.core.matcher.NoArgumentsMatcher;
import org.jmock.core.stub.CustomStub;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.TestFailureStub;

public class OrderedInvocationDispatcher implements InvocationDispatcher {
	public static final String NO_EXPECTATIONS_MESSAGE = "No expectations set";
    
	private InvokablesCollection invokables;
	private Stub defaultStub = new TestFailureStub("no match found");

	public OrderedInvocationDispatcher(InvokablesCollection invokables) {
		this.invokables = invokables;
	}

	public Object dispatch(Invocation invocation) throws Throwable {
		InvokablesCollection.Iterator i = invokables.iterator();
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
		InvokablesCollection.Iterator i = invokables.iterator();
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
		InvokablesCollection.Iterator iterator = invokables.iterator();
		while (iterator.hasMore()) {
			Invokable invokable = iterator.next();
			if (invokable.hasDescription()) {
				invokable.describeTo(buffer).append("\n");
			}
		}
	}

	private boolean anyInvokableHasDescription() {
		InvokablesCollection.Iterator iterator = invokables.iterator();
		while (iterator.hasMore()) {
			if (iterator.next().hasDescription()) {
				return true;
			}
		}
		return false;
	}

    // ----------------------------------------------------------
    public void setupDefaultBehaviour(String name, Object proxy) {
        add(hiddenInvocationMocker("toString", NoArgumentsMatcher.INSTANCE, new ReturnStub(name)));
        add(hiddenInvocationMocker("equals",
                                            new ArgumentsMatcher(new Constraint[]{new IsAnything()}),
                                            new IsSameAsProxyStub(proxy)));
        add(hiddenInvocationMocker("hashCode",
                                            NoArgumentsMatcher.INSTANCE,
                                            new HashCodeStub()));
    }

    private static final InvocationMocker.Describer NO_DESCRIPTION =
        new InvocationMocker.Describer()
        {
            public boolean hasDescription() {
                return false;
            }

            public void describeTo( StringBuffer buffer, List matchers, Stub stub, String name ) {
            }
        };

    private InvocationMocker hiddenInvocationMocker( String methodName,
                                                     InvocationMatcher arguments,
                                                     Stub stub )
    {
        InvocationMocker invocationMocker = new InvocationMocker(NO_DESCRIPTION);

        invocationMocker.addMatcher(new MethodNameMatcher(methodName));
        invocationMocker.addMatcher(arguments);
        invocationMocker.setStub(stub);

        return invocationMocker;
    }

    private class IsSameAsProxyStub extends CustomStub
    {
        private Object proxy;
        
        private IsSameAsProxyStub(Object proxy) {
            super("returns whether equal to proxy");
            this.proxy = proxy;
        }

        public Object invoke( Invocation invocation ) throws Throwable {
            return new Boolean(invocation.parameterValues.get(0) == proxy);
        }
    }

    private class HashCodeStub extends CustomStub
    {
        private HashCodeStub() {
            super("returns hashCode for proxy");
        }

        public Object invoke( Invocation invocation ) throws Throwable {
            return new Integer(OrderedInvocationDispatcher.this.hashCode());
        }
    }
    // ----------------------------------------------------------
	static public class FIFOInvokablesCollection implements InvokablesCollection {
		private List list = new ArrayList();

		public InvokablesCollection.Iterator iterator() {
			return new InvokablesCollection.Iterator() {
				private java.util.Iterator iterator = list.iterator();

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

		public InvokablesCollection.Iterator iterator() {
			return new InvokablesCollection.Iterator() {
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