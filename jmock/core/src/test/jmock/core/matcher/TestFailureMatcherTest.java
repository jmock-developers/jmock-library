/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.core.matcher;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.core.Invocation;
import org.jmock.core.matcher.TestFailureMatcher;

public class TestFailureMatcherTest 
	extends TestCase 
{
	static final String MESSAGE = "MESSAGE";
	
	Invocation invocation;
	TestFailureMatcher testFailureMatcher; 
	
	public void setUp() {
		invocation = new Invocation( 
            "INVOKED-OBJECT", Void.class, "ignoredName",  new Class[0], 
			void.class, new Object[0]);
		testFailureMatcher  = new TestFailureMatcher(MESSAGE);
	}
	
    public void testAlwaysMatches() {
        assertTrue( "matches", testFailureMatcher.matches(invocation) );
        invokeMatcher();
        assertTrue( "matches", testFailureMatcher.matches(invocation) );
    }
    
    public void testAlwaysVerifies() throws Throwable {
        testFailureMatcher.verify();
        invokeMatcher();
        testFailureMatcher.verify();
    }
    
    public void testThrowsAssertionFailedErrorWhenInvoked() throws Throwable {
		try {
			testFailureMatcher.invoked(invocation);
		} catch (AssertionFailedError ex ) {
            assertEquals( "should be error message from stub",
                          MESSAGE, ex.getMessage() );
            return;
		}
        fail("expected AssertionFailedError");
	}
	
	public void testUsesErrorMessageAsDescription() {
		StringBuffer buffer = new StringBuffer();
		
		testFailureMatcher.describeTo(buffer);
		
		assertEquals( "description", MESSAGE, buffer.toString() );
	}
    
    private void invokeMatcher() {
        try {
            testFailureMatcher.invoked(invocation);
        } catch (AssertionFailedError ex ) {
            // expected
        }
    }
}
