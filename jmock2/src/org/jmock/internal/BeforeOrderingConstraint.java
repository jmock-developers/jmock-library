package org.jmock.internal;

import java.util.Set;

import org.hamcrest.Description;
import org.jmock.api.Expectation;

public class BeforeOrderingConstraint extends AbstractOrderingConstraint {
    public BeforeOrderingConstraint(ExpectationNamespace namespace, Set<String> subsequentExpectationNames) {
        super(namespace, subsequentExpectationNames);
    }
    
    @Override
    protected boolean otherExpectationAllowsInvocation(Expectation expectation) {
        return !expectation.hasBeenInvoked();
    }
    
    public void describeTo(Description description) {
        describeOrderingConstraint("before", description);
    }
}
