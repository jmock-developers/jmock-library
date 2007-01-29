package org.jmock.internal;

import java.util.Set;
import java.util.TreeSet;

import org.hamcrest.Description;
import org.jmock.api.Expectation;

public abstract class AbstractOrderingConstraint implements OrderingConstraint {
    private final ExpectationNamespace namespace;
    private final Set<String> namesOfOtherExpectations;
    
    public AbstractOrderingConstraint(ExpectationNamespace namespace, Set<String> namesOfOtherExpectations) {
        this.namespace = namespace;
        this.namesOfOtherExpectations = namesOfOtherExpectations;
    }

    public boolean allowsInvocationNow() {
        for (String name : namesOfOtherExpectations) {
            Expectation expectation = namespace.resolve(name);
            if (!otherExpectationAllowsInvocation(expectation)) return false;
        }
        return true;
    }
    
    protected abstract boolean otherExpectationAllowsInvocation(Expectation expectation);
    
    protected void describeOrderingConstraint(String type, Description description) {
        description.appendText(type);
        description.appendText(" ");
        
        Set<String> namesInOrder = new TreeSet<String>(namesOfOtherExpectations);
        
        boolean separate = false;
        for (String name : namesInOrder) {
            if (separate) description.appendText(", ");
            description.appendText(name);
            separate = true;
        }
    }
}
