/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.matcher;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.matcher.InvokeOnceMatcher;

public class InvokeOnceMatcherTest extends TestCase {
    private Invocation emptyInvocation = new Invocation(
        "INVOKED-OBJECT", Void.class, "example", new Class[0], Void.class, 
        new Object[0]);
    private InvokeOnceMatcher matcher = new InvokeOnceMatcher();
    
    public void testWillMatchIfNotYetInvoked() {
        assertTrue("Should match", matcher.matches(emptyInvocation));
    }
    
    public void testVerifyFailsIfNotYetInvoked() {
        try {
            matcher.verify();
        } catch (AssertionFailedError expected) {
            return;
        }
        fail("Should have thrown exception");
    }
    
    public void testWillNotMatchAfterInvocation() {
        matcher.invoked(emptyInvocation);
        assertFalse("Should not match", matcher.matches(emptyInvocation));
    }

    public void testVerifyPassesAfterInvocation() {
        matcher.invoked(emptyInvocation);
        matcher.verify();
    }
    
    private static final String MATCH_DESCRIPTION = "expected once";
    
    public void testWritesDescriptionOfMatch() {
    	String description = matcher.describeTo(new StringBuffer()).toString();
    	
        assertTrue( "should have description", matcher.hasDescription() );
    	assertEquals( "should describe match", MATCH_DESCRIPTION, description );
    }
    
    public void testReportsWhetherCalledInDescription() {
        matcher.invoked(emptyInvocation);
        
        String description = matcher.describeTo(new StringBuffer()).toString();
        
        assertTrue( "should have description", matcher.hasDescription() );
        assertTrue( "should describe match", 
                    description.indexOf(MATCH_DESCRIPTION) >= 0 );
        assertTrue( "should report has been called",
                    description.indexOf("has been invoked") >= 0 );
    }
}
