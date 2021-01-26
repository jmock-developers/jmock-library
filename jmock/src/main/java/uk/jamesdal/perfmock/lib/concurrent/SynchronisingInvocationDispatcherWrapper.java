package uk.jamesdal.perfmock.lib.concurrent;

import org.hamcrest.Description;
import uk.jamesdal.perfmock.api.Expectation;
import uk.jamesdal.perfmock.api.Invocation;
import uk.jamesdal.perfmock.api.InvocationDispatcher;
import uk.jamesdal.perfmock.internal.StateMachine;

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
