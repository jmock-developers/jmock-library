package org.jmock.internal;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsAnything;
import org.jmock.api.Action;
import org.jmock.api.Expectation;
import org.jmock.api.Invocation;
import org.jmock.internal.matcher.MethodMatcher;
import org.jmock.lib.action.VoidAction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/** 
 * An expectation of zero or more matching invocations.
 * 
 * @author npryce
 * @author smgf
 */
public class InvocationExpectation implements Expectation {
    private static ParametersMatcher ANY_PARAMETERS = new AnyParametersMatcher();
    private Cardinality cardinality = Cardinality.ALLOWING;
	private Matcher<?> objectMatcher = IsAnything.anything();
	private Matcher<? super Method> methodMatcher = IsAnything.anything("<any method>");
	private boolean methodIsKnownToBeVoid = false;
	private ParametersMatcher parametersMatcher = ANY_PARAMETERS;
    private Action action = new VoidAction();
    private boolean actionIsDefault = true;
    private List<OrderingConstraint> orderingConstraints = new ArrayList<OrderingConstraint>();
    private List<SideEffect> sideEffects = new ArrayList<SideEffect>();
    
	private int invocationCount = 0;
	
    public void setCardinality(Cardinality cardinality) {
        this.cardinality = cardinality;
    }
	
	public void setObjectMatcher(Matcher<?> objectMatcher) {
		this.objectMatcher = objectMatcher;
	}
	
	public void setMethod(Method method) {
	    this.methodMatcher = new MethodMatcher(method);
	    this.methodIsKnownToBeVoid = method.getReturnType() == void.class;
	}
	
	public void setMethodMatcher(Matcher<? super Method> matcher) {
		this.methodMatcher = matcher;
		this.methodIsKnownToBeVoid = false;
	}
	
	public void setParametersMatcher(ParametersMatcher parametersMatcher) {
		this.parametersMatcher = parametersMatcher;
	}

    public void addOrderingConstraint(OrderingConstraint orderingConstraint) {
        orderingConstraints.add(orderingConstraint);
    }

    public void addSideEffect(SideEffect sideEffect) {
        sideEffects.add(sideEffect);
    }
    
    public void setAction(Action action) {
        this.action = action;
        this.actionIsDefault = false;
    }
    
    public void setDefaultAction(Action action) {
        this.action = action;
        this.actionIsDefault = true;
    }
    
    public void describeTo(Description description) {
        if (! isSatisfied()) {
            description.appendText("! ");
        }

        describeExpectation(description);
    }

    public void describeMismatch(Invocation invocation, Description description) {
        describeExpectation(description);

        final Object[] parameters = invocation.getParametersAsArray();
        if (methodMatcher.matches(invocation.getInvokedMethod()) &&
            parametersMatcher.isCompatibleWith(parameters))
        {
            parametersMatcher.describeMismatch(parameters, description);
        }
    }

    private void describeExpectation(Description description) {
        describeMethod(description);
        parametersMatcher.describeTo(description);
        describeSideEffects(description);
    }

    private void describeMethod(Description description) {
        cardinality.describeTo(description);
        description.appendText(", ");
        if (invocationCount == 0) {
            description.appendText("never invoked");
        }
        else {
            description.appendText("already invoked ");
            description.appendText(Formatting.times(invocationCount));
        }
        description.appendText(": ");
        objectMatcher.describeTo(description);
        description.appendText(".");
        methodMatcher.describeTo(description);
    }
    
    private void describeSideEffects(Description description) {
        for (OrderingConstraint orderingConstraint : orderingConstraints) {
            description.appendText("; ");
            orderingConstraint.describeTo(description);
        }
        
        if (!shouldSuppressActionDescription()) {
            description.appendText("; ");
            action.describeTo(description);
        }
        
        for (SideEffect sideEffect : sideEffects) {
            description.appendText("; ");
            sideEffect.describeTo(description);
        }
    }

    private boolean shouldSuppressActionDescription() {
        return methodIsKnownToBeVoid && actionIsDefault;
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
		performSideEffects();
		final Object result = action.invoke(invocation);
        invocation.checkReturnTypeCompatibility(result);
        return result;
	}

    private void performSideEffects() {
        for (SideEffect sideEffect : sideEffects) {
            sideEffect.perform();
        }
    }
    
    private static class AnyParametersMatcher extends IsAnything<Object[]> implements ParametersMatcher {
        public AnyParametersMatcher() {
            super("(<any parameters>)");
        }

        public boolean isCompatibleWith(Object[] parameters) {
            return true;
        }
    };

}
