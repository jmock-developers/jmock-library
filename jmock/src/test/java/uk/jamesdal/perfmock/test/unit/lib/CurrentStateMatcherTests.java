package uk.jamesdal.perfmock.test.unit.lib;

import static org.hamcrest.StringDescription.asString;

import org.hamcrest.Matcher;
import uk.jamesdal.perfmock.States;
import uk.jamesdal.perfmock.internal.StateMachine;
import uk.jamesdal.perfmock.lib.CurrentStateMatcher;


public class CurrentStateMatcherTests extends AbstractMatcherTest {
    States stateMachine = new StateMachine("stateMachine");
    Matcher<States> isCurrentlyS = CurrentStateMatcher.isCurrently("S");
    Matcher<States> isNotCurrentlyS = CurrentStateMatcher.isNotCurrently("S");
    
    public void testMatchesStateMachineCurrentlyInNamedState() {
        stateMachine.become("S");
        
        assertTrue("should match", CurrentStateMatcher.isCurrently("S").matches(stateMachine));
        assertTrue("should not match", !CurrentStateMatcher.isNotCurrently("S").matches(stateMachine));
    }
    
    public void testDoesNotMatchStateMachineCurrentlyInOtherState() {
        stateMachine.become("T");
        
        assertTrue("should not match", !CurrentStateMatcher.isCurrently("S").matches(stateMachine));
        assertTrue("should match", CurrentStateMatcher.isNotCurrently("S").matches(stateMachine));
    }

    public void testDoesNotMatchStateMachineInAnonymousInitialState() {
        assertTrue("should not match", !CurrentStateMatcher.isCurrently("S").matches(stateMachine));
        assertTrue("should match", CurrentStateMatcher.isNotCurrently("S").matches(stateMachine));
    }

    public void testDoesNotMatchNull() {
        assertTrue("should not match", !isCurrentlyS.matches(null));
    }

    public void testDoesNotMatchOtherTypesOfObject() {
        assertTrue("should not match", !isCurrentlyS.matches("something else"));
    }
    
    public void testHasReadableDescription() {
        assertEquals("a state machine that is S", asString(CurrentStateMatcher.isCurrently("S")));
        assertEquals("a state machine that is not S", asString(CurrentStateMatcher.isNotCurrently("S")));
    }
    
    public void testHasReadableDescriptionWhenPassedToAssertThat() {
        stateMachine.become("X");
        
        assertMismatchDescription("was not S", CurrentStateMatcher.isCurrently("S"), stateMachine);
    }

    @Override
    protected Matcher<?> createMatcher() {
        return isCurrentlyS;
    }
}
