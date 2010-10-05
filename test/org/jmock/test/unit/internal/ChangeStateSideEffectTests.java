package org.jmock.test.unit.internal;

import junit.framework.TestCase;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.jmock.internal.ChangeStateSideEffect;
import org.jmock.internal.State;


public class ChangeStateSideEffectTests extends TestCase {
    FakeState state = new FakeState();
    ChangeStateSideEffect sideEffect = new ChangeStateSideEffect(state);

    public void testActivatesTheGivenState() {
        
        state.isActive = false;
        sideEffect.perform();
        assertTrue("state should be active", state.isActive);
    }
    
    public void testDescribesItselfInTermsOfTheActivatedState() {
        state.descriptionText = "the-new-state";
        
        assertEquals("description", "then the-new-state", StringDescription.toString(sideEffect));
    }
    
    class FakeState implements State {
        public boolean isActive = false;
        
        public void activate() {
            isActive = true;
        }

        public boolean isActive() {
            return isActive;
        }

        public String descriptionText;
        public void describeTo(Description description) {
            description.appendText(descriptionText);
        }
    }
}
