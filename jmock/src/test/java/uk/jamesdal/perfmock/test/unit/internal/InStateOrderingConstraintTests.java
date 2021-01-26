package uk.jamesdal.perfmock.test.unit.internal;

import junit.framework.TestCase;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import uk.jamesdal.perfmock.internal.StatePredicate;
import uk.jamesdal.perfmock.internal.InStateOrderingConstraint;

public class InStateOrderingConstraintTests extends TestCase {
    FakeStatePredicate statePredicate = new FakeStatePredicate();
    InStateOrderingConstraint orderingConstraint = new InStateOrderingConstraint(statePredicate);

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
