package org.jmock.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.jmock.core.Action;
import org.jmock.core.Invocation;
import org.jmock.lib.InvocationExpectation;
import org.jmock.syntax.MethodClause;
import org.jmock.syntax.ParametersClause;
import org.jmock.syntax.ReceiverClause;

public class InvocationExpectationBuilder implements ExpectationCapture, 
    ReceiverClause, MethodClause, ParametersClause
{
    private final InvocationExpectation expectation;
    
    private boolean isFullySpecified = false;
    private DispatcherControl dispatcher = null;
    private List<Matcher<?>> capturedParameterMatchers = new ArrayList<Matcher<?>>();
    
    protected InvocationExpectationBuilder(InvocationExpectation expectation) {
        this.expectation = expectation;
    }
    
    public void setDefaultAction(Action defaultAction) {
        expectation.setAction(defaultAction);
    }
    
    private <T> void captureExpectedObject(T mockObject) {
        if (!(mockObject instanceof DispatcherControl)) {
            throw new IllegalArgumentException("can only set expectations on mock objects");
        }
        
        dispatcher = (DispatcherControl)mockObject;
        
        expectation.setObjectMatcher(new MockObjectMatcher(mockObject));
        
        isFullySpecified = true;
        
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
        if (!isFullySpecified) {
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

    public MethodClause of(Matcher<Object> objectMatcher) {
        expectation.setObjectMatcher(objectMatcher);
        isFullySpecified = true;
        return this;
    }

    public ParametersClause method(Matcher<Method> methodMatcher) {
        expectation.setMethodMatcher(methodMatcher);
        return this;
    }
    
    public void with(Matcher<?>... parameterMatchers) {
        expectation.setParametersMatcher(new ParametersMatcher(Arrays.asList(parameterMatchers)));
    }
}
