package org.jmock.internal;

import org.hamcrest.Description;

public class InStateOrderingConstraint implements OrderingConstraint {
    private final StatePredicate statePredicate;

    public InStateOrderingConstraint(StatePredicate statePredicate) {
        this.statePredicate = statePredicate;
    }

    public boolean allowsInvocationNow() {
        return statePredicate.isActive();
    }

    public void describeTo(Description description) {
        description.appendText("when ");
        statePredicate.describeTo(description);
    }
}
