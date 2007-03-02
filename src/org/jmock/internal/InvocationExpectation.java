package org.jmock.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
    private String name = null;
    private List<OrderingConstraint> orderingConstraints = new ArrayList<OrderingConstraint>();
    private List<SideEffect> sideEffects = new ArrayList<SideEffect>();
    
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

    public void setName(String name) {
        this.name = name;
    }
    
    public void addOrderingConstraint(OrderingConstraint orderingConstraint) {
        orderingConstraints.add(orderingConstraint);
    }

    public void addSideEffect(SideEffect sideEffect) {
        sideEffects.add(sideEffect);
    }
    
    public void setAction(Action action) {
        this.action = action;
    }
    
    public void describeTo(Description description) {
        if (name != null) {
            description.appendText(name);
            description.appendText(" = ");
        }
        
        cardinality.describeTo(description);
        description.appendText(", invoked ");
        description.appendText(Integer.toString(invocationCount));
        description.appendText(times(invocationCount));
        description.appendText(": ");
        objectMatcher.describeTo(description);
        description.appendText(".");
        methodMatcher.describeTo(description);
        parametersMatcher.describeTo(description);
        for (OrderingConstraint orderingConstraint : orderingConstraints) {
            description.appendText("; ");
            orderingConstraint.describeTo(description);
        }
        description.appendText("; ");
        action.describeTo(description);
        for (SideEffect sideEffect : sideEffects) {
            description.appendText("; ");
            sideEffect.describeTo(description);
        }
    }

    private static String times(int n) {
        return (n == 1) ? " time" : " times";
    }
    
    public boolean hasBeenInvoked() {
        return invocationCount > 0;
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
			&& parametersMatcher.matches(invocation.getParametersAsArray())
            && isInCorrectOrder();
        
	}
    
	private boolean isInCorrectOrder() {
        for (OrderingConstraint constraint : orderingConstraints) {
            if (!constraint.allowsInvocationNow()) return false;
        }
        return true;
    }
	
    public Object invoke(Invocation invocation) throws Throwable {
		invocationCount++;
		final Object result = action.invoke(invocation);
        invocation.checkReturnTypeCompatibility(result);
        performSideEffects();
        return result;
	}

    private void performSideEffects() {
        for (SideEffect sideEffect : sideEffects) {
            sideEffect.perform();
        }
    }
}
