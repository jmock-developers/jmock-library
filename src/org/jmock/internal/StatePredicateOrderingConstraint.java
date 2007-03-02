package org.jmock.internal;

import org.hamcrest.Description;

public class StatePredicateOrderingConstraint implements OrderingConstraint {
    private final StatePredicate statePredicate;

    public StatePredicateOrderingConstraint(StatePredicate statePredicate) {
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
