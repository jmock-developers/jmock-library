package org.jmock.lib;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.jmock.api.Action;
import org.jmock.api.Expectation;
import org.jmock.api.Invocation;
import org.jmock.lib.action.VoidAction;

/** 
 * An expectation of zero or more matching invocations.
 * 
 * @author npryce
 * @author smgf
 */
public class InvocationExpectation implements Expectation {
    private Cardinality cardinality = Cardinality.ALLOWING;
	private Matcher<Object> objectMatcher = IsAnything.anything();
	private Matcher<Method> methodMatcher = IsAnything.anything("<any method>");
	private Matcher<Object[]> parametersMatcher = IsAnything.anything("(<any parameters>)");
    private Action action = new VoidAction();
    
	private int invocationCount = 0;

    public void setCardinality(Cardinality cardinality) {
        this.cardinality = cardinality;
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
        cardinality.describeTo(description);
        description.appendText(", invoked ");
        description.appendText(Integer.toString(invocationCount));
        description.appendText(times(invocationCount));
        description.appendText(": ");
        objectMatcher.describeTo(description);
        description.appendText(".");
        methodMatcher.describeTo(description);
        parametersMatcher.describeTo(description);
        description.appendText(" [");
        action.describeTo(description);
        description.appendText("]");
    }

    private static String times(int n) {
        return (n == 1) ? " time" : " times";
    }
    
    public boolean isSatisfied() {
        return cardinality.isSatisfied(invocationCount);
    }
    
    public boolean allowsMoreInvocations() {
        return cardinality.allowsMoreInvocations(invocationCount);
    }
    
    public boolean matches(Invocation invocation) {
		return allowsMoreInvocations()
			&& objectMatcher.matches(invocation.getInvokedObject())
			&& methodMatcher.matches(invocation.getInvokedMethod())
			&& parametersMatcher.matches(invocation.getParametersAsArray());
	}
    
	public Object invoke(Invocation invocation) throws Throwable {
		invocationCount++;
		final Object result = action.invoke(invocation);
        invocation.checkReturnTypeCompatibility(result);
        return result;
	}
}
