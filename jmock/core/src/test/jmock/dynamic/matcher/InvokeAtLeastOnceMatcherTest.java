package test.jmock.dynamic.matcher;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.matcher.InvokeAtLeastOnceMatcher;
import org.jmock.expectation.AssertMo;


public class InvokeAtLeastOnceMatcherTest extends TestCase {
    private Invocation emptyInvocation = new Invocation(
        "INVOKED-OBJECT", Void.class, "example", new Class[0], Void.class, 
        new Object[0]);
	private InvokeAtLeastOnceMatcher matcher = new InvokeAtLeastOnceMatcher();

	public void testAlwaysMatches() {
		assertTrue("should match", matcher.matches(emptyInvocation));
		matcher.invoked(emptyInvocation);
		assertTrue("should still match", matcher.matches(emptyInvocation));
	}
	
	public void testVerifyFailsIfNotYetInvoked() {
        try {
            matcher.verify();
        } catch (AssertionFailedError ex) {
            AssertMo.assertIncludes( "should report method not invoked",
                "expected method was not invoked", ex.getMessage() );
            return;
        }
        fail("Should have thrown exception");
	}
	
	public void testVerifyPassesAfterInvocation() {
		matcher.invoked(emptyInvocation);
		matcher.verify();
	}
	
	public void testVerifyPassesAfterMultipleInvocations() {
		matcher.invoked(emptyInvocation);
		matcher.invoked(emptyInvocation);
		matcher.invoked(emptyInvocation);
		matcher.verify();
	}
	
    private static final String MATCH_DESCRIPTION = "expected at least once";
    
	public void testWritesDescriptionOfMatch() {
		String description = matcher.describeTo(new StringBuffer()).toString();
		
        assertTrue( "should have description", matcher.hasDescription() );
		assertEquals( "should describe match", MATCH_DESCRIPTION, description );
	}
    
    public void testReportsWhetherInvokedInDescription() {
        matcher.invoked(emptyInvocation);
        
        String description = matcher.describeTo(new StringBuffer()).toString();
        
        assertTrue( "should have description", matcher.hasDescription() );
        assertTrue( "should describe match", 
                    description.indexOf(MATCH_DESCRIPTION) >= 0 );
        assertTrue( "should report has been called", 
                    description.indexOf("has been invoked") >= 0 );
    }
}
