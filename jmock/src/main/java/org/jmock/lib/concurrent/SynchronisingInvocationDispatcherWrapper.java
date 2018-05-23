package org.jmock.lib.concurrent;

import org.hamcrest.Description;
import org.jmock.api.Expectation;
import org.jmock.api.Invocation;
import org.jmock.api.InvocationDispatcher;
import org.jmock.internal.StateMachine;

/**
 * I synchronise my delegate.
 * This serialises access to the internal Expectation and StateMachine collections.
 * @author oliverbye
 *
 */
public class SynchronisingInvocationDispatcherWrapper implements InvocationDispatcher {

    private final InvocationDispatcher delegate;

    public SynchronisingInvocationDispatcherWrapper(InvocationDispatcher dispatcher) {
        delegate = dispatcher;
    }
    
    public synchronized StateMachine newStateMachine(String name) {
        return delegate.newStateMachine(name);
    }

    public synchronized void add(Expectation expectation) {
        delegate.add(expectation);
    }

    public synchronized void describeTo(Description description) {
        delegate.describeTo(description);
    }

    public synchronized void describeMismatch(Invocation invocation, Description description) {
        delegate.describeMismatch(invocation, description);
    }

    public synchronized boolean isSatisfied() {
        return delegate.isSatisfied();
    }

    public synchronized Object dispatch(Invocation invocation) throws Throwable {
        return delegate.dispatch(invocation);
    }
}
