package org.jmock.lib;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.jmock.core.Action;
import org.jmock.core.Expectation;
import org.jmock.core.Invocation;
import org.jmock.lib.action.VoidAction;

/** 
 * An expectation of zero or more matching invocations.
 * 
 * @author npryce
 */
public class InvocationExpectation implements Expectation {
    private int requiredInvocationCount = 0;
    private int maximumInvocationCount = Integer.MAX_VALUE;
	private Matcher<Object> objectMatcher = IsAnything.anything();
	private Matcher<Method> methodMatcher = IsAnything.anything("<any method>");
	private Matcher<Object[]> parametersMatcher = IsAnything.anything("(<any parameters>)");
    private Action action = new VoidAction();
    
	private int invocationCount = 0;

    public void setCardinality(int requiredInvocationCount, int maximumInvocationCount) {
        this.requiredInvocationCount = requiredInvocationCount;
        this.maximumInvocationCount = maximumInvocationCount;
    }
	
	public void setObjectMatcher(Matcher<Object> objectMatcher) {
		this.objectMatcher = objectMatcher;
	}
	
	public void setMethodMatcher(Matcher<Method> methodMatcher) {
		this.methodMatcher = methodMatcher;
	}
	
	public void setParametersMatcher(Matcher<Object[]> parametersMatcher) {
		this.parametersMatcher = parametersMatcher;
	}
	
    public void setAction(Action action) {
        this.action = action;
    }
    
    public void describeTo(Description description) {
        describeCardinality(description);
        description.appendText(": ");
        objectMatcher.describeTo(description);
        description.appendText(".");
        methodMatcher.describeTo(description);
        parametersMatcher.describeTo(description);
        description.appendText(" [");
        action.describeTo(description);
        description.appendText("]");
    }

    private void describeCardinality(Description description) {
        int min = requiredInvocationCount;
        int max = maximumInvocationCount;
        
        if (min == 0 && max == 0) {
            description.appendText("never");
        }
        else if (min == 0 && max == Integer.MAX_VALUE) {
            description.appendText("allowed");
        }
        if (min == max) {
            description.appendText("exactly ");
            description.appendText(Integer.toString(min));
        }
        else if (max == Integer.MAX_VALUE) {
            description.appendText("at least ");
            description.appendText(Integer.toString(min));
        }
        else if (min == 0) {
            description.appendText("at most ");
            description.appendText(Integer.toString(max));
        }
        else {
            description.appendText(Integer.toString(min));
            description.appendText(" to ");
            description.appendText(Integer.toString(max));
        }
    }
    
    public boolean needsMoreInvocations() {
        return invocationCount < requiredInvocationCount;
    }
    
    public boolean allowsMoreInvocations() {
        return invocationCount < maximumInvocationCount;
    }
    
    public boolean matches(Invocation invocation) {
		return allowsMoreInvocations()
			&& objectMatcher.match(invocation.getInvokedObject())
			&& methodMatcher.match(invocation.getInvokedMethod())
			&& parametersMatcher.match(invocation.getParametersAsArray());
	}
    
	public Object invoke(Invocation invocation) throws Throwable {
		invocationCount++;
		return action.invoke(invocation);
	}
}
