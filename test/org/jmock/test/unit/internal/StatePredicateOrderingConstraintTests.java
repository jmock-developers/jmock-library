package org.jmock.test.unit.internal;

import junit.framework.TestCase;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.jmock.internal.StatePredicate;
import org.jmock.internal.StatePredicateOrderingConstraint;

public class StatePredicateOrderingConstraintTests extends TestCase {
    FakeStatePredicate statePredicate = new FakeStatePredicate();
    StatePredicateOrderingConstraint orderingConstraint = new StatePredicateOrderingConstraint(statePredicate);

    public void testAllowsInvocationWhenStateIsActive() {
        
        statePredicate.isActive = true;
        assertTrue("should allow invocation when state predicate is true",
                   orderingConstraint.allowsInvocationNow());
        
        statePredicate.isActive = false;
        assertTrue("should not allow invocation when state predicate is false",
                   !orderingConstraint.allowsInvocationNow());
    }
    
    public void testDescribesItselfInTermsOfTheStatePredicatesDescription() {
        statePredicate.descriptionText = "the-predicate";
        
        assertEquals("description", "when the-predicate", StringDescription.toString(orderingConstraint));
    }
    
    class FakeStatePredicate implements StatePredicate {
        public boolean isActive;
        
        public boolean isActive() {
            return isActive;
        }

        public String descriptionText;
        public void describeTo(Description description) {
            description.appendText(descriptionText);
        }
    }
}
