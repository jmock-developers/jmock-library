/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.matcher;

import junit.framework.TestCase;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.matcher.InvokedRecorder;
import org.jmock.expectation.AssertMo;

public class InvokedRecorderTest extends TestCase {
    private Invocation emptyInvocation = new Invocation(
        "INVOKED-OBJECT", 
        Void.class, "example", new Class[0], Void.class, 
        new Object[0]);
    InvokedRecorder recorder = new InvokedRecorder();
    
    public void testAlwaysMatches() {
        assertTrue( "should match before invocation", 
                    recorder.matches(emptyInvocation));
        
        recorder.invoked(emptyInvocation);
        
        assertTrue( "should still match after invocation", 
                    recorder.matches(emptyInvocation));
    }
    
    public void testAlwaysVerifies() {
    	recorder.verify();
        recorder.invoked(emptyInvocation);
        recorder.verify();
    }
    
    public void testReportsIfHasBeenInvoked() {
    	assertFalse( "should not have been invoked", recorder.hasBeenInvoked() );
        recorder.invoked(emptyInvocation);
        assertTrue( "should have been invoked", recorder.hasBeenInvoked () );
    }
    
    public void testCanExplicitlyVerifyThatItHasBeenInvoked() {
    	AssertMo.assertFails("verifyHasBeenInvoked should fail", new Runnable() {
    		public void run() {
    			recorder.verifyHasBeenInvoked();
    		}
    	} );
        recorder.invoked(emptyInvocation);
        recorder.verifyHasBeenInvoked();
    }
    
    public void testDoesNotWriteDescription() {
    	assertEquals( "should not be any description",
    				  "", (recorder.describeTo(new StringBuffer())).toString() );
    }
}
