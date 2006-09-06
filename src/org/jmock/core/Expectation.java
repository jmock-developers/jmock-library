package org.jmock.core;

import org.hamcrest.SelfDescribing;

public interface Expectation extends SelfDescribing {
	boolean matches(Invocation invocation);
	boolean isSatisfied();
	Object invoke(Invocation invocation) throws Throwable;
}
