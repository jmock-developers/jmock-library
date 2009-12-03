package org.jmock.test.unit.lib;

import static org.hamcrest.StringDescription.asString;
import static org.jmock.lib.CurrentStateMatcher.isCurrently;
import static org.jmock.lib.CurrentStateMatcher.isNotCurrently;

import org.hamcrest.AbstractMatcherTest;
import org.hamcrest.Matcher;
import org.jmock.States;
import org.jmock.internal.StateMachine;


public class CurrentStateMatcherTests extends AbstractMatcherTest {
    States stateMachine = new StateMachine("stateMachine");
    Matcher<States> isCurrentlyS = isCurrently("S");
    Matcher<States> isNotCurrentlyS = isNotCurrently("S");
    
    public void testMatchesStateMachineCurrentlyInNamedState() {
        stateMachine.become("S");
        
        assertTrue("should match", isCurrently("S").matches(stateMachine));
        assertTrue("should not match", !isNotCurrently("S").matches(stateMachine));
    }
    
    public void testDoesNotMatchStateMachineCurrentlyInOtherState() {
        stateMachine.become("T");
        
        assertTrue("should not match", !isCurrently("S").matches(stateMachine));
        assertTrue("should match", isNotCurrently("S").matches(stateMachine));
    }

    public void testDoesNotMatchStateMachineInAnonymousInitialState() {
        assertTrue("should not match", !isCurrently("S").matches(stateMachine));
        assertTrue("should match", isNotCurrently("S").matches(stateMachine));
    }

    public void testDoesNotMatchNull() {
        assertTrue("should not match", !isCurrentlyS.matches(null));
    }

    public void testDoesNotMatchOtherTypesOfObject() {
        assertTrue("should not match", !isCurrentlyS.matches("something else"));
    }
    
    public void testHasReadableDescription() {
        assertEquals("a state machine that is S", asString(isCurrently("S")));
        assertEquals("a state machine that is not S", asString(isNotCurrently("S")));
    }
    
    public void testHasReadableDescriptionWhenPassedToAssertThat() {
        stateMachine.become("X");
        
        assertMismatchDescription("was not S", isCurrently("S"), stateMachine);
    }

    @Override
    protected Matcher<?> createMatcher() {
        return isCurrentlyS;
    }
}
