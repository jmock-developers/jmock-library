package org.jmock.lib;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jmock.States;

public class CurrentStateMatcher extends TypeSafeMatcher<States> {
    private final String stateName;
    private final boolean expected;
    
    public CurrentStateMatcher(String stateName, boolean expected) {
        this.expected = expected;
        this.stateName = stateName;
    }
    
    @Override
    public boolean matchesSafely(States stateMachine) {
        return stateMachine.is(stateName).isActive() == expected;
    }
    
    @Override
    protected void describeMismatchSafely(States stateMachine, Description mismatchDescription) {
        mismatchDescription.appendText("was ");
        if (!stateMachine.is(stateName).isActive()) {
            mismatchDescription.appendText("not ");
        }
        mismatchDescription.appendText(stateName);
    }
    
    public void describeTo(Description description) {
        description.appendText("a state machine that ")
                   .appendText(expected ? "is " : "is not ")
                   .appendText(stateName);
    }
    
    @Factory
    public static Matcher<States> isCurrently(String stateName) {
        return new CurrentStateMatcher(stateName, true);
    }
    
    @Factory 
    public static Matcher<States> isNotCurrently(String stateName) {
        return new CurrentStateMatcher(stateName, false);
    }

}
