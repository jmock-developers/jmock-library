package org.jmock.test.unit.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jmock.api.Expectation;
import org.jmock.api.ExpectationError;
import org.jmock.api.Invocation;
import org.jmock.api.InvocationDispatcher;
import org.jmock.internal.StateMachine;
import org.jmock.lib.concurrent.SynchronisingInvocationDispatcherWrapper;
import org.jmock.lib.concurrent.UnsynchronisedInvocationDispatcher;
import org.jmock.test.unit.support.MethodFactory;
import org.jmock.test.unit.support.MockExpectation;

import junit.framework.TestCase;

public class InvocationDispatcherTests extends TestCase {

    // Avoid multi threaeding tests deadlocking
    // Adjust timeout for debugging
    private static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;
    private static final int TIMEOUT = 2;

    MethodFactory methodFactory = new MethodFactory();
    Invocation invocation = new Invocation(
            "invokedObject",
            methodFactory.newMethod("invokedMethod"),
            Invocation.NO_PARAMETERS);

    static final boolean NOT_RELEVANT = true;

    public void testInvokesFirstMatchingExpectationInGroup() throws Throwable {
        MockExpectation expectation1 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation2 = new MockExpectation(true, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation3 = new MockExpectation(true, NOT_RELEVANT, NOT_RELEVANT);

        UnsynchronisedInvocationDispatcher dispatcher = new UnsynchronisedInvocationDispatcher();
        dispatcher.add(expectation1);
        dispatcher.add(expectation2);
        dispatcher.add(expectation3);

        expectation1.shouldNotBeInvoked();
        expectation2.shouldBeInvokedWith(invocation);
        expectation3.shouldNotBeInvoked();

        dispatcher.dispatch(invocation);

        assertTrue("expectation2 should have been invoked",
                expectation2.wasInvoked);
    }

    public void testThrowsExpectationErrorIfNoExpectationsMatchAnInvocation() throws Throwable {
        MockExpectation expectation1 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation2 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation3 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);

        UnsynchronisedInvocationDispatcher dispatcher = new UnsynchronisedInvocationDispatcher();
        dispatcher.add(expectation1);
        dispatcher.add(expectation2);
        dispatcher.add(expectation3);

        expectation1.shouldNotBeInvoked();
        expectation2.shouldNotBeInvoked();
        expectation3.shouldNotBeInvoked();

        try {
            dispatcher.dispatch(invocation);
            fail("should have thrown ExpectationError");
        } catch (ExpectationError e) {
            // expected
        }
    }

    public void testIsSatisfiedOnlyIfAllExpectationsAreSatisfied() {
        UnsynchronisedInvocationDispatcher dispatcherAll = new UnsynchronisedInvocationDispatcher();
        dispatcherAll.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        dispatcherAll.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        assertTrue("should be satisfied if all expectations are satisfied",
                dispatcherAll.isSatisfied());

        UnsynchronisedInvocationDispatcher dispatcher1 = new UnsynchronisedInvocationDispatcher();
        dispatcher1.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        dispatcher1.add(new MockExpectation(NOT_RELEVANT, false, NOT_RELEVANT));
        assertFalse("should not be satisfied if first expectation is not satisfied",
                dispatcher1.isSatisfied());

        UnsynchronisedInvocationDispatcher dispatcher2 = new UnsynchronisedInvocationDispatcher();
        dispatcher2.add(new MockExpectation(NOT_RELEVANT, false, NOT_RELEVANT));
        dispatcher2.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        assertFalse("should not be satisfied if second expectation is not satisfied",
                dispatcher2.isSatisfied());

        UnsynchronisedInvocationDispatcher dispatcherNone = new UnsynchronisedInvocationDispatcher();
        dispatcherNone.add(new MockExpectation(NOT_RELEVANT, false, NOT_RELEVANT));
        dispatcherNone.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        assertFalse("should not be satisfied if no expectations are satisfied",
                dispatcherNone.isSatisfied());
    }

    /**
     * Resolves issue 104
     * 
     * @throws Throwable
     */
    public void testUnsynchronisedInvocationDispatcherHandlesAddingExpectationsWhileOtherTestsDispatch()
            throws Throwable {

        final CyclicBarrier barrier = new CyclicBarrier(2);

        MockExpectation expectation1 = new MockExpectation(true, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation2 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);

        Collection<Expectation> expectations = new CriticalSectionForcingCollectionWrapper<>(
                new CopyOnWriteArrayList<Expectation>(), barrier);
        Collection<StateMachine> stateMachines = new CriticalSectionForcingCollectionWrapper<>(
                new ArrayList<StateMachine>(), barrier);
        final UnsynchronisedInvocationDispatcher dispatcher = new UnsynchronisedInvocationDispatcher(expectations,
                stateMachines);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    barrier.await(TIMEOUT, TIMEOUT_UNIT);
                    barrier.await(TIMEOUT, TIMEOUT_UNIT);
                    // now the expectation one has been added

                    dispatcher.dispatch(invocation);
                    barrier.await(TIMEOUT, TIMEOUT_UNIT);
                } catch (Throwable e) {
                    // will throw a ConcurrentModification Exception unless a multithreaded strategy
                    // is used
                    throw new RuntimeException(e);
                }
            }
        }, "Concurrent Dispatch").start();

        // expect dispatch
        dispatcher.add(expectation1);
        // await is satisfied check

        dispatcher.add(expectation2);
        barrier.await(TIMEOUT, TIMEOUT_UNIT);

        expectation1.shouldBeInvokedWith(invocation);
        assertTrue("expectation1 should have been invoked",
                expectation1.wasInvoked);
    }

    public void testSynchronisedInvocationDispatcherBlocksAddingExpectationsWhileOtherTestsDispatch()
            throws Throwable {

        final CyclicBarrier barrier = new CyclicBarrier(2);
        
        MockExpectation expectation1 = new MockExpectation(true, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation2 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);

        // intentionally use array list as the wrapper should synchronise access
        final InvocationDispatcher dispatcher = new SynchronisingInvocationDispatcherWrapper(
                new UnsynchronisedInvocationDispatcher(new ArrayList<Expectation>(), new ArrayList<StateMachine>()));

        // expect dispatch
        dispatcher.add(expectation1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dispatcher.dispatch(invocation);
                    barrier.await();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Concurrent Dispatch").start();

        // await until dispatch
        barrier.await();

        dispatcher.add(expectation2);

        expectation1.shouldBeInvokedWith(invocation);
        assertTrue("expectation1 should have been invoked",
                expectation1.wasInvoked);
    }

    private class CriticalSectionForcingCollectionWrapper<T> implements Collection<T> {
        private final Collection<T> delegate;
        private final CyclicBarrier barrier;

        CriticalSectionForcingCollectionWrapper(Collection<T> delegate, CyclicBarrier barrier) {
            this.delegate = delegate;
            this.barrier = barrier;
        }

        private void await() {
            try {
                // we want the expectation check to have got the iterator
                // but not progressed checking
                barrier.await(TIMEOUT, TIMEOUT_UNIT);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        }

        public int size() {
            return delegate.size();
        }

        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        public boolean contains(Object o) {
            return delegate.contains(o);
        }

        public Iterator<T> iterator() {
            Iterator<T> reply = delegate.iterator();
            await(); // expectation add follows this
            await(); // wait for add to complete
            return reply;
        }

        public Object[] toArray() {
            return delegate.toArray();
        }

        public <U> U[] toArray(U[] a) {
            return delegate.toArray(a);
        }

        public boolean add(T e) {
            // Make sure iterator is called before adding
            await();
            boolean reply = delegate.add(e);
            // Make sure iterator returns after adding
            await();
            return reply;
        }

        public boolean remove(Object o) {
            return delegate.remove(o);
        }

        public boolean containsAll(Collection<?> c) {
            return delegate.containsAll(c);
        }

        public boolean addAll(Collection<? extends T> c) {
            return delegate.addAll(c);
        }

        public boolean removeAll(Collection<?> c) {
            return delegate.removeAll(c);
        }

        public boolean retainAll(Collection<?> c) {
            return delegate.retainAll(c);
        }

        public void clear() {
            delegate.clear();
        }

        public boolean equals(Object o) {
            return delegate.equals(o);
        }

        public int hashCode() {
            return delegate.hashCode();
        }
    }

}
