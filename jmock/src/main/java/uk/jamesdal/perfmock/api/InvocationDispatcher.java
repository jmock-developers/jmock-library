package uk.jamesdal.perfmock.api;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import uk.jamesdal.perfmock.internal.StateMachine;

public interface InvocationDispatcher extends SelfDescribing, ExpectationCollector {

    StateMachine newStateMachine(String name);

    void add(Expectation expectation);

    void describeTo(Description description);

    void describeMismatch(Invocation invocation, Description description);

    boolean isSatisfied();

    Object dispatch(Invocation invocation) throws Throwable;
}