package test.jmock.dynamic.matcher;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.matcher.InvokedAfterMatcher;
import org.jmock.dynamic.matcher.InvokedRecorder;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;


public class InvokedAfterMatcherTest extends TestCase {
	private static final String PRIOR_CALL_ID = "RECORDER-ID";
	
	private Invocation invocation1 =
		new Invocation( Void.class, "caller", 
						"invocation1", new Class[0], Void.class, new Object[0] );
	private Invocation invocation2 =
		new Invocation( Void.class, "caller", 
						"invocation2", new Class[0], Void.class, new Object[0] );
	
	private InvokedRecorder recorder;
	private InvokedAfterMatcher after;
	
	
	public void setUp() {
		recorder = new InvokedRecorder();
		after = new InvokedAfterMatcher( recorder, PRIOR_CALL_ID );
	}
	
	public void testOnlyMatchesWhenInvokedAfterPriorInvocation() {
		assertFalse( "should not match before previous invocation", 
                     after.matches(invocation2) );
		recorder.invoked(invocation1);
		assertTrue( "should match after previous invocation", 
                    after.matches(invocation2) );
	}
	
    // Note: this will not happen in typical usage because it will not match out of order
	public void testFailsIfInvokedOutOfOrder() {
		try {
			after.invoked(invocation2);
		}
		catch( AssertionFailedError ex ) {
			assertTrue( "should contain description of prior call",
						ex.getMessage().indexOf(PRIOR_CALL_ID) >= 0 );
			return;
		}
		fail("AssertionFailedError expected");
	}
	
	public void testIdentifiesPriorCallInDescription() {
		StringBuffer buf = new StringBuffer();
		
		after.writeTo(buf);
		
		assertTrue( "should include 'after <id of prior call>' in description",
					buf.toString().indexOf("after "+PRIOR_CALL_ID) >= 0 );
	}
}
