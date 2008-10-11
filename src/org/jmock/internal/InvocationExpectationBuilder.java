package org.jmock.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.jmock.Sequence;
import org.jmock.api.Action;
import org.jmock.api.Expectation;
import org.jmock.api.Invocation;
import org.jmock.internal.matcher.MethodNameMatcher;
import org.jmock.internal.matcher.MockObjectMatcher;
import org.jmock.internal.matcher.AllParametersMatcher;
import org.jmock.syntax.MethodClause;
import org.jmock.syntax.ParametersClause;
import org.jmock.syntax.ReceiverClause;

public class InvocationExpectationBuilder 
    implements ExpectationCapture, 
               ReceiverClause, MethodClause, ParametersClause
{
    private final InvocationExpectation expectation = new InvocationExpectation();
    
    private boolean isFullySpecified = false;
    private boolean needsDefaultAction = true;
    private List<Matcher<?>> capturedParameterMatchers = new ArrayList<Matcher<?>>();
    
    public Expectation toExpectation(Action defaultAction) {
        if (needsDefaultAction) {
            expectation.setDefaultAction(defaultAction);
        }
        
        return expectation;
    }
    
    public void setCardinality(Cardinality cardinality) {
        expectation.setCardinality(cardinality);
    }
    
    public void addParameterMatcher(Matcher<?> matcher) {
        capturedParameterMatchers.add(matcher);
    }
    
    public void addOrderingConstraint(OrderingConstraint constraint) {
        expectation.addOrderingConstraint(constraint);
    }
    
    public void addInSequenceOrderingConstraint(Sequence sequence) {
        sequence.constrainAsNextInSequence(expectation);
    }
    
    public void setAction(Action action) {
        expectation.setAction(action);
        needsDefaultAction = false;
    }
    
    public void addSideEffect(SideEffect sideEffect) {
        expectation.addSideEffect(sideEffect);
    }
    
    private <T> T captureExpectedObject(T mockObject) {
        if (!(mockObject instanceof CaptureControl)) {
            throw new IllegalArgumentException("can only set expectations on mock objects");
        }
        
        expectation.setObjectMatcher(new MockObjectMatcher(mockObject));
        isFullySpecified = true;
        
        Object capturingImposter = ((CaptureControl)mockObject).captureExpectationTo(this);
        
        return asMockedType(mockObject, capturingImposter);
    }
    
    // Damn you Java generics! Damn you to HELL!
    @SuppressWarnings("unchecked")
    private <T> T asMockedType(@SuppressWarnings("unused") T mockObject, 
                               Object capturingImposter) 
    {
        return (T) capturingImposter;
    }
    
    public void createExpectationFrom(Invocation invocation) {
        expectation.setMethod(invocation.getInvokedMethod());
        
        if (capturedParameterMatchers.isEmpty()) {
            expectation.setParametersMatcher(new AllParametersMatcher(invocation.getParametersAsArray()));
        }
        else {
            checkParameterMatcherCount(invocation);
            expectation.setParametersMatcher(new AllParametersMatcher(capturedParameterMatchers));
        }
    }
    
    private void checkParameterMatcherCount(Invocation invocation) {
        if (capturedParameterMatchers.size() != invocation.getParameterCount()) {
            throw new IllegalArgumentException("not all parameters were given explicit matchers: either all parameters must be specified by matchers or all must be specified by values, you cannot mix matchers and values");
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
        return captureExpectedObject(mockObject);
    }

    public MethodClause of(Matcher<?> objectMatcher) {
        expectation.setObjectMatcher(objectMatcher);
        isFullySpecified = true;
        return this;
    }

    public ParametersClause method(Matcher<Method> methodMatcher) {
        expectation.setMethodMatcher(methodMatcher);
        return this;
    }
    
    public ParametersClause method(String nameRegex) {
        return method(new MethodNameMatcher(nameRegex));
    }
    
    public void with(Matcher<?>... parameterMatchers) {
        expectation.setParametersMatcher(new AllParametersMatcher(Arrays.asList(parameterMatchers)));
    }
    
    public void withNoArguments() {
        with();
    }
}
