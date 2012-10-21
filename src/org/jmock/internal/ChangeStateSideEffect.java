package org.jmock.internal;

import org.hamcrest.Description;

public class ChangeStateSideEffect implements SideEffect {
    private final State state;

    public ChangeStateSideEffect(State state) {
        this.state = state;
    }

    public void perform() {
        state.activate();
    }

    public void describeTo(Description description) {
        description.appendText("then ");
        state.describeTo(description);
    }
}
