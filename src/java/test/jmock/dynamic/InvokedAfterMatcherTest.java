package test.jmock.dynamic;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.matcher.InvokedAfterMatcher;
import org.jmock.dynamic.matcher.InvokedRecorder;

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
	
	public void testDoesNotMatchBeforeCallToOtherInvocation() {		
		assertFalse( "should not match", after.matches(invocation2) );
	}
	
	public void testMatchesAfterCallToOtherInvocation() {
		recorder.invoked(invocation1);
		
		assertTrue( "should now match", after.matches(invocation2) );
	}
	
	public void testIdentifiesPriorCallInDescription() {
		StringBuffer buf = new StringBuffer();
		
		after.writeTo(buf);
		
		assertTrue( "should include 'after <id of prior call>' in description",
					buf.toString().indexOf("after "+PRIOR_CALL_ID) >= 0 );
	}
}
