package org.jmock.internal;

import java.util.Set;

import org.hamcrest.Description;
import org.jmock.api.Expectation;

public class AfterOrderingConstraint extends AbstractOrderingConstraint {
    public AfterOrderingConstraint(ExpectationNamespace namespace, Set<String> precedingExpectationNames) {
        super(namespace, precedingExpectationNames);
    }
    
    @Override
    protected boolean otherExpectationAllowsInvocation(Expectation otherExpectation) {
        return otherExpectation.hasBeenInvoked() && otherExpectation.isSatisfied();
    }
    
    public void describeTo(Description description) {
        describeOrderingConstraint("after", description);
    }
}
