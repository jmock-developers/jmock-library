package org.jmock.api;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

/**
 * An error thrown when an expectation is violated during a test.
 * 
 * @author npryce
 */
public class ExpectationError extends Error implements SelfDescribing {
    public final SelfDescribing expectations;
    public final Invocation invocation;
	
    public ExpectationError(String message, SelfDescribing expectations, Invocation invocation) {
        super(message);
        this.invocation = invocation;
        this.expectations = expectations;
    }
    
    public ExpectationError(String message, Invocation invocation) {
        this(message, null, invocation);
    }
    
    public ExpectationError(String message, SelfDescribing expectations) {
        this(message, expectations, null);
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
        if (expectations != null) {
            description.appendText("\n");
            expectations.describeTo(description);
        }
	}
}
