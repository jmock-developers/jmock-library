package org.jmock.dynamic.matcher;

import junit.framework.Assert;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.InvocationMatcher;


public class InvokedRecorder 
    implements InvocationMatcher 
{
    private boolean hasBeenInvoked = false;
    
    public boolean hasBeenInvoked() {
        return hasBeenInvoked;
    }
    
    public boolean matches(Invocation invocation) {
		return true;
	}
    
	public void invoked(Invocation invocation) {
        hasBeenInvoked = true;
	}
	
    public boolean hasDescription() {
        return false;
    }
    
	public StringBuffer describeTo(StringBuffer buffer) {
		return buffer;
	}
	
	public void verify() {
        // always verifies
	}

	public void verifyHasBeenInvoked() {
		Assert.assertTrue( "expected method was not invoked", hasBeenInvoked );
	}
}
