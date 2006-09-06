package org.jmock.test.unit.support;

import junit.framework.Assert;

import org.hamcrest.Description;
import org.jmock.core.Expectation;
import org.jmock.core.Invocation;

public class MockExpectation extends Assert implements Expectation {
	public boolean matches;
	public boolean isSatisfied;
	
    public MockExpectation() {
        this(false, false);
    }
	
    public MockExpectation(boolean matches, boolean isSatisfied) {
		this.matches = matches;
		this.isSatisfied = isSatisfied;
	}
	
	public boolean matches(Invocation invocation) {
		return matches;
	}
	
	public boolean isSatisfied() {
		return isSatisfied;
	}
	
    private boolean shouldBeInvoked = true;
    private Invocation expectedInvocation = null;
    public Object invokeResult = null;
    public boolean wasInvoked = false;
    
    public void shouldNotBeInvoked() {
        shouldBeInvoked = false;
    }
    
    public void shouldBeInvokedWith(Invocation invocation) {
        shouldBeInvoked = true;
        expectedInvocation = invocation;
    }
    
	public Object invoke(Invocation invocation) throws Throwable {
        assertTrue("should not have been invoked; invocation: " + invocation, 
                   shouldBeInvoked);
        
        if (expectedInvocation != null) {
            assertSame("unexpected invocation", expectedInvocation, invocation);
        }
        wasInvoked = true;
        return invokeResult;
	}

    public void describeTo(Description description) {
        throw new UnsupportedOperationException("not implemented");
    }

}
