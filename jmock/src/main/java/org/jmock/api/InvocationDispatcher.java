package org.jmock.api;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.jmock.internal.StateMachine;

public interface InvocationDispatcher extends SelfDescribing, ExpectationCollector {

    StateMachine newStateMachine(String name);

    void add(Expectation expectation);

    void describeTo(Description description);

    void describeMismatch(Invocation invocation, Description description);

    boolean isSatisfied();

    Object dispatch(Invocation invocation) throws Throwable;
}