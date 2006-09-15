package org.jmock.internal;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.jmock.core.Action;
import org.jmock.core.ExpectationCollection;
import org.jmock.core.Invocation;
import org.jmock.lib.InvocationExpectation;
import org.jmock.syntax.ReceiverClause;

public class InvocationExpectationBuilder implements ExpectationCapture, ReceiverClause {
    private final ExpectationCollection collection;
    private final InvocationExpectation expectation;
    
    private DispatcherControl dispatcher = null;
    private List<Matcher<?>> capturedParameterMatchers = new ArrayList<Matcher<?>>();
    
    protected InvocationExpectationBuilder(InvocationExpectation expectation, ExpectationCollection collection) {
        this.expectation = expectation;
        this.collection = collection;
    }
    
    public void setDefaultAction(Action defaultAction) {
        expectation.setAction(defaultAction);
    }
    
    private <T> void captureExpectedObject(T mockObject) {
        if (!(mockObject instanceof DispatcherControl)) {
            throw new IllegalArgumentException("can only set expectations on mock objects");
        }
        
        dispatcher = (DispatcherControl)mockObject;
        collection.add(expectation);
        
        expectation.setObjectMatcher(new MockObjectMatcher(mockObject));

        dispatcher.startCapturingExpectations(this);
    }
    
    public void addParameterMatcher(Matcher<?> matcher) {
        capturedParameterMatchers.add(matcher);
    }
    
    public void createExpectationFrom(Invocation invocation) {
        expectation.setMethodMatcher(new MethodMatcher(invocation.getInvokedMethod()));
        
        if (capturedParameterMatchers.isEmpty()) {
            expectation.setParametersMatcher(new ParametersMatcher(invocation.getParametersAsArray()));
        }
        else {
            checkParameterMatcherCount(invocation);
            expectation.setParametersMatcher(new ParametersMatcher(capturedParameterMatchers));
        }
        
        dispatcher.stopCapturingExpectations();
    }
    
    private void checkParameterMatcherCount(Invocation invocation) {
        if (capturedParameterMatchers.size() != invocation.getParameterCount()) {
            throw new IllegalArgumentException("not all parameters were given explicit constraints: either all parameters must be specified by explicit constraints or all must be specified by literal values to match");
        }
    }
    
    public void checkWasFullySpecified() {
        if (dispatcher == null) {
            throw new IllegalStateException("expectation was not fully specified");
        }
    }

    /* 
     * Syntactic sugar
     */
    
    public <T> T of(T mockObject) {
        captureExpectedObject(mockObject);
        return mockObject;
    }

}
