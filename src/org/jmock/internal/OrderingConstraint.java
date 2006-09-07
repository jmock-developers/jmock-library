package org.jmock.internal;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsSame;
import org.jmock.core.Action;
import org.jmock.core.ExpectationGroup;
import org.jmock.core.Invocation;
import org.jmock.lib.InvocationExpectation;
import org.jmock.lib.action.ActionSequence;
import org.jmock.lib.action.DoAllAction;
import org.jmock.lib.action.ReturnValueAction;
import org.jmock.lib.action.ThrowAction;

public class OrderingConstraint implements ExpectationCapture  {
    private final ExpectationGroup expectationGroup;
    
    private ContextControl context = null;
    private Action defaultAction = null;
    private InvocationExpectation invocationExpectation;
    private List<Matcher<?>> capturedParameterMatchers = new ArrayList<Matcher<?>>();
    private boolean waitingForObject = false;
    private boolean waitingForInvocation = false; // implies waiting for object
    
    public void setDefaultAction(Action defaultAction) {
        this.defaultAction = defaultAction;
    }
    
    protected OrderingConstraint(ExpectationGroup expectationGroup) {
        this.expectationGroup = expectationGroup;
    }
    
    private void initialiseExpectationCapture() {
        if (waitingForObject) {
            throw new IllegalStateException("incomplete expectation");
        }
        
        capturedParameterMatchers.clear();
        invocationExpectation = new InvocationExpectation();
        expectationGroup.add(invocationExpectation);
        
        waitingForObject = true;
        waitingForInvocation = false;
    }
    
    private <T> void captureExpectedObject(T mockObject) {
        if (!(mockObject instanceof ContextControl)) {
            throw new IllegalArgumentException("can only set expectations on mock objects");
        }
        
        if (!waitingForObject) {
            throw new IllegalStateException("incomplete expectation");
        }
        
        if (context == null) {
            context = (ContextControl)mockObject;
            context.startCapturingExpectations(this);
        }
        
        invocationExpectation.setObjectMatcher(new MockObjectMatcher(mockObject));
        
        waitingForObject = false;
        waitingForInvocation = true;
    }
    
    private <T> void captureParameterMatcher(Matcher<T> matcher) {
        if (!waitingForInvocation) {
            throw new IllegalStateException("incomplete expectation");
        }
        
        capturedParameterMatchers.add(matcher);
    }
    
    public void createExpectationFrom(Invocation invocation) {
        if (waitingForObject || !waitingForInvocation) {
            throw new IllegalStateException("no expectation has been specified");
        }
        
        invocationExpectation.setMethodMatcher(new MethodMatcher(invocation.getInvokedMethod()));
        
        if (capturedParameterMatchers.isEmpty()) {
            invocationExpectation.setParametersMatcher(new ParametersMatcher(invocation.getParametersAsArray()));
        }
        else {
            checkContraintCount(invocation);
            invocationExpectation.setParametersMatcher(new ParametersMatcher(capturedParameterMatchers));
        }
        
        invocationExpectation.setAction(defaultAction);
        
        waitingForInvocation = false;
    }
    
    private void checkContraintCount(Invocation invocation) {
        if (capturedParameterMatchers.size() != invocation.getParameterCount()) {
            throw new IllegalArgumentException("not enough constraints specified");
        }
    }
    
    public void stopCapturingExpectations() {
        if (waitingForObject) {
            throw new IllegalStateException("no expectation has been specified");
        }
        
        if (context != null) {
            context.setExpectation(expectationGroup);
        }
    }
    
    /* 
     * Syntactic sugar
     */
    
    protected OrderingConstraint exactly(int count) {
        initialiseExpectationCapture();
        invocationExpectation.setMaxInvocationCount(count);
        invocationExpectation.setRequiredInvocationCount(count);
        invocationExpectation.setCardinalityDescription("exactly "+count);
        return this;
    }
    
    protected OrderingConstraint atLeast(int count) {
        initialiseExpectationCapture();
        invocationExpectation.setRequiredInvocationCount(count);
        invocationExpectation.setCardinalityDescription("at least "+count);
        return this;
    }
    
    protected OrderingConstraint between(int minCount, int maxCount) {
        initialiseExpectationCapture();
        invocationExpectation.setMaxInvocationCount(maxCount);
        invocationExpectation.setRequiredInvocationCount(minCount);
        invocationExpectation.setCardinalityDescription("between "+minCount + " and " + maxCount);
        return this;
    }
    
    protected OrderingConstraint atMost(int count) {
        initialiseExpectationCapture();
        invocationExpectation.setMaxInvocationCount(count);
        invocationExpectation.setRequiredInvocationCount(0);
        invocationExpectation.setCardinalityDescription("at most "+count);
        return this;
    }
    
    public <T> T allow(T mockObject) {
        T result = atLeast(0).of(mockObject);
        invocationExpectation.setCardinalityDescription("allow");
        return result;
    }
    
    public <T> void ignore(T mockObject) {
        atLeast(0).of(mockObject);
        invocationExpectation.setCardinalityDescription("ignore");
    }

    public <T> T never(T mockObject) {
        T result = exactly(0).of(mockObject);
        invocationExpectation.setCardinalityDescription("never");
        return result;
    }

    public <T> T of(T mockObject) {
        captureExpectedObject(mockObject);
        return mockObject;
    }

    public <T> T with(Matcher<T> matcher) {
        captureParameterMatcher(matcher);
        return null;
    }

    public boolean with(Matcher<Boolean> matcher) {
        captureParameterMatcher(matcher);
        return Boolean.FALSE;
    }
    
    public byte with(Matcher<Byte> matcher) {
        captureParameterMatcher(matcher);
        return 0;
    }

    public short with(Matcher<Short> matcher) {
        captureParameterMatcher(matcher);
        return 0;
    }

    public int with(Matcher<Integer> matcher) {
        captureParameterMatcher(matcher);
        return 0;
    }

    public long with(Matcher<Long> matcher) {
        captureParameterMatcher(matcher);
        return 0;
    }

    public float with(Matcher<Float> matcher) {
        captureParameterMatcher(matcher);
        return 0.0f;
    }

    public double with(Matcher<Double> matcher) {
        captureParameterMatcher(matcher);
        return 0.0;
    }

    public void will(Action action) {
        invocationExpectation.setAction(action);
    }
    
    public void to(Action action) {
        will(action);
    }
    
    public void expects(OrderingConstraint orderingConstraint) {
        // not implemented
    }
    
    /* Common constraints
     */
    
    public <T> Matcher<T> equal(T value) {
        return IsEqual.eq(value);
    }
    
    public <T> Matcher<T> same(T value) {
        return IsSame.same(value);
    }
    
    public <T> Matcher<T> any(Class<T> type) {
        return IsAnything.anything();
    }
    
    public <T> Matcher<T> anything() {
        return IsAnything.anything();
    }
    
    public Matcher<?> a(Class<?> type) {
        return IsInstanceOf.isA(type);
    }
    
    public Matcher<?> an(Class<?> type) {
        return IsInstanceOf.isA(type);
    }
    
    /* Common actions
     */
    
    public Action returnValue(Object result) {
        return new ReturnValueAction(result);
    }
    
    public Action throwException(Throwable throwable) {
        return new ThrowAction(throwable);
    }
    
    public Action doAll(Action...actions) {
        return new DoAllAction(actions);
    }
    
    public Action onConsecutiveCalls(Action...actions) {
        return new ActionSequence(actions);
    }
}
