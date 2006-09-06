package org.jmock.core;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

/**
 * An error thrown when an expectation is violated during a test.
 * 
 * @author npryce
 */
public class ExpectationError extends Error implements SelfDescribing {
	public final Invocation invocation;
    public final Expectation expectation;
	
    public ExpectationError(String message, Expectation expectation, Invocation invocation) {
        super(message);
        this.invocation = invocation;
        this.expectation = expectation;
    }

    public ExpectationError(String message, Expectation expectation) {
        this(message, expectation, null);
    }

    @Override 
    public String toString() {
        return StringDescription.toString(this);
    }
    
	public void describeTo(Description description) {
		description.appendText(getMessage());
        if (invocation != null) {
            description.appendText(": ");
            invocation.describeTo(description);
        }
        description.appendText("\nexpected ");
        expectation.describeTo(description);
	}
}
