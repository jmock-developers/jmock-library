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
import org.jmock.internal.matcher.MethodMatcher;
import org.jmock.lib.action.VoidAction;

/** 
 * An expectation of zero or more matching invocations.
 * 
 * @author npryce
 * @author smgf
 */
public class InvocationExpectation implements Expectation {
    private Cardinality cardinality = Cardinality.ALLOWING;
	private Matcher<?> objectMatcher = IsAnything.anything();
	private Matcher<Method> methodMatcher = IsAnything.anything("<any method>");
	private boolean methodIsKnownToBeVoid = false;
	private ParametersMatcher parametersMatcher = new AnyParameters();
    private Action action = new VoidAction();
    private boolean actionIsDefault = true;
    private List<OrderingConstraint> orderingConstraints = new ArrayList<OrderingConstraint>();
    private List<SideEffect> sideEffects = new ArrayList<SideEffect>();
    
	private int invocationCount = 0;
	
	public static class AnyParameters extends IsAnything<Object[]> implements ParametersMatcher {
        public AnyParameters() { super("(<any parameters>)"); }
	};
	
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
	
	public void setMethodMatcher(Matcher<Method> methodMatcher) {
		this.methodMatcher = methodMatcher;
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
        parametersMatcher.describeTo(description);
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


    public void describeMismatch(Object item, Description description) {
        describeTo(description);
        description.appendText("\n");
        Invocation invocation = (Invocation)item;
        parametersMatcher.describeMismatch(invocation.getParametersAsArray(), description);
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
}
