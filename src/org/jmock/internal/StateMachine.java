package org.jmock.internal;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.jmock.States;

public class StateMachine implements States {
    private final String name;
    private String currentState = null;
    
    public StateMachine(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return StringDescription.asString(this);
    }

    public States startsAs(String initialState) {
        become(initialState);
        return this;
    }
    
    public void become(String nextState) {
        currentState = nextState;
    }

    public State is(final String state) {
        return new State() {
            public void activate() {
                currentState = state;
            }

            public boolean isActive() {
                return state.equals(currentState);
            }

            public void describeTo(Description description) {
                description.appendText(name).appendText(" is ").appendText(state);
            }
        };
    }
    
    public StatePredicate isNot(final String state) {
        return new StatePredicate() {
            public boolean isActive() {
                return !state.equals(currentState);
            }

            public void describeTo(Description description) {
                description.appendText(name).appendText(" is not ").appendText(state);
            }
        };
    }
    
    public void describeTo(Description description) {
        description.appendText(name)
                   .appendText(currentState == null ? " has no current state" : (" is " + currentState));
    }
}
