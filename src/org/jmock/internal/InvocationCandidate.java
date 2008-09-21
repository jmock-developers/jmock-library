/**
 * 
 */
package org.jmock.internal;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.jmock.api.Expectation;
import org.jmock.api.ExpectationError;
import org.jmock.api.Invocation;

public abstract class InvocationCandidate implements SelfDescribing {
    public final Invocation invocation;
    
    abstract public Object invoke() throws Throwable;
    
    protected InvocationCandidate(Invocation invocation) {
        this.invocation = invocation;
    }
    
    public static class ExpectationFound  extends InvocationCandidate {
        private final Expectation expectation;
        public ExpectationFound(Invocation invocation, Expectation expectation) {
            super(invocation);
            this.expectation = expectation;
        }
        
        @Override public Object invoke() throws Throwable {
            return expectation.invoke(invocation);
        }

        public void describeTo(Description description) {
            expectation.describeMismatch(invocation, description);
        }
    }
    
    public static class ExpectationMissing extends InvocationCandidate {
        public ExpectationMissing(Invocation invocation) {
            super(invocation);
        }
        @Override public Object invoke() throws Throwable {
            throw new ExpectationError("unexpected invocation", invocation);
        }
        public void describeTo(Description description) {
            description.appendDescriptionOf(invocation);
        }
    }
}