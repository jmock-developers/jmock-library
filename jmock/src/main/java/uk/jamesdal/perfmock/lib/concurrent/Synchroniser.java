package uk.jamesdal.perfmock.lib.concurrent;

import static org.hamcrest.StringDescription.asString;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeoutException;

import uk.jamesdal.perfmock.api.Expectation;
import uk.jamesdal.perfmock.api.Invocation;
import uk.jamesdal.perfmock.api.InvocationDispatcher;
import uk.jamesdal.perfmock.api.Invokable;
import uk.jamesdal.perfmock.api.ThreadingPolicy;
import uk.jamesdal.perfmock.internal.StateMachine;
import uk.jamesdal.perfmock.internal.StatePredicate;
import uk.jamesdal.perfmock.lib.concurrent.internal.FixedTimeout;
import uk.jamesdal.perfmock.lib.concurrent.internal.InfiniteTimeout;
import uk.jamesdal.perfmock.lib.concurrent.internal.Timeout;

/**
 * A ThreadingPolicy that makes the Mockery thread-safe and helps tests
 * synchronise with background threads.
 * 
 * @author Nat Pryce
 * @author olibye
 */
public class Synchroniser implements ThreadingPolicy {
    private final Object sync = new Object();
    private Error firstError = null;
    private InvocationDispatcher invocationDispatcher;

    public Synchroniser() {
        invocationDispatcher = new UnsynchronisedInvocationDispatcher(
                new CopyOnWriteArrayList<Expectation>(),
                new CopyOnWriteArrayList<StateMachine>());
    }

    public Synchroniser(InvocationDispatcher dispatcher) {
        invocationDispatcher = dispatcher;
    }

    /**
     * Waits for a StatePredicate to become active.
     * 
     * Warning: this will wait forever unless the test itself has a timeout.
     * 
     * @param p
     *            the StatePredicate to wait for
     * @throws InterruptedException
     */
    public void waitUntil(StatePredicate p) throws InterruptedException {
        waitUntil(p, new InfiniteTimeout());
    }

    /**
     * Waits up to a timeout for a StatePredicate to become active. Fails the test
     * if the timeout expires.
     * 
     * @param p the StatePredicate to wait for
     * @param timeoutMs
     *            the timeout in milliseconds
     * @throws InterruptedException
     */
    public void waitUntil(StatePredicate p, long timeoutMs) throws InterruptedException {
        waitUntil(p, new FixedTimeout(timeoutMs));
    }

    private void waitUntil(StatePredicate p, Timeout timeout) throws InterruptedException {
        synchronized (sync) {
            while (!p.isActive()) {
                try {
                    sync.wait(timeout.timeRemaining());
                } catch (TimeoutException e) {
                    if (firstError != null) {
                        throw firstError;
                    } else {
                        throw new AssertionError("timed out waiting for " + asString(p));
                    }
                }
            }
        }

    }

    public Invokable synchroniseAccessTo(final Invokable mockObject) {
        return new Invokable() {
            public Object invoke(Invocation invocation) throws Throwable {
                return synchroniseInvocation(mockObject, invocation);
            }
        };
    }

    private Object synchroniseInvocation(Invokable mockObject, Invocation invocation) throws Throwable {
        synchronized (sync) {
            try {
                return mockObject.invoke(invocation);
            } catch (Error e) {
                if (firstError == null) {
                    firstError = e;
                }
                throw e;
            } finally {
                sync.notifyAll();
            }
        }
    }

    public InvocationDispatcher dispatcher() {
        return invocationDispatcher;
    }
}
