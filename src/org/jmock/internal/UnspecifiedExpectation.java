package org.jmock.internal;

import org.hamcrest.Description;
import org.jmock.api.Expectation;
import org.jmock.api.Invocation;

public class UnspecifiedExpectation implements Expectation {
    public static final String ERROR = "no expectations have been specified";

    public boolean matches(Invocation invocation) {
        throw new IllegalStateException(ERROR);
    }

    public void describeTo(Description description) {
        description.appendText(ERROR);
    }
    
    public boolean allowsMoreInvocations() {
        return false;
    }
    
    public boolean isSatisfied() {
        return true;
    }
    
    public Object invoke(Invocation invocation) throws Throwable {
        throw new IllegalStateException(ERROR);
    }
}
