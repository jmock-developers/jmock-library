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
	
	public StringBuffer writeTo(StringBuffer buffer) {
		return buffer;
	}
	
	public void verify() {
        // always verifies
	}

	public void verifyHasBeenInvoked() {
		Assert.assertTrue( "should have been invoked", hasBeenInvoked );
	}

	public void verifyHasNotBeenInvoked() {
		Assert.assertFalse( "should not have been invoked", hasBeenInvoked );
	}
}
