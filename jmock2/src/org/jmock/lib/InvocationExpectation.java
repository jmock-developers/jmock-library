package org.jmock.lib;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.hamcrest.number.IsComparableTo;
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
	private Matcher<Integer> satisfiedCountMatcher = IsAnything.anything();
	private Matcher<Integer> matchingCountMatcher = IsAnything.anything();
    private String cardinalityDescription = "";
	private Matcher<Object> objectMatcher = IsAnything.anything();
	private Matcher<Method> methodMatcher = IsAnything.anything("<any method>");
	private Matcher<Object[]> parametersMatcher = IsAnything.anything("(<any parameters>)");
    private Action action = new VoidAction();
    
	private int invocationCount = 0;
	
    public void setCardinalityDescription(String description) {
        this.cardinalityDescription = description;
    }
    
	public void setMaxInvocationCount(int n) {
		matchingCountMatcher = IsComparableTo.lessThan(n);
	}
	
	public void setRequiredInvocationCount(int n) {
		satisfiedCountMatcher = IsComparableTo.greaterThanOrEqualTo(n);
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
        if (cardinalityDescription != null) {
            description.appendText(cardinalityDescription);
        }
        else {
            description.appendText("between ");
            satisfiedCountMatcher.describeTo(description);
            description.appendText(" and ");
            matchingCountMatcher.describeTo(description);
        }
        description.appendText(": ");
        objectMatcher.describeTo(description);
        description.appendText(".");
        methodMatcher.describeTo(description);
        parametersMatcher.describeTo(description);
        description.appendText(" [");
        action.describeTo(description);
        description.appendText("]");
    }
    
    public boolean isSatisfied() {
        return satisfiedCountMatcher.match(invocationCount);
    }
    
    public boolean isActive() {
        return matchingCountMatcher.match(invocationCount);
    }
    
    public boolean matches(Invocation invocation) {
		return isActive()
			&& objectMatcher.match(invocation.getInvokedObject())
			&& methodMatcher.match(invocation.getInvokedMethod())
			&& parametersMatcher.match(invocation.getParametersAsArray());
	}
    
	public Object invoke(Invocation invocation) throws Throwable {
		invocationCount++;
		return action.invoke(invocation);
	}
}
