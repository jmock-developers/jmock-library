package test.jmock.dynamic.matcher;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.matcher.InvokeAtLeastOnceMatcher;


public class InvokeAtLeastOnceMatcherTest extends TestCase {
	private Invocation emptyInvocation =
		new Invocation(Void.class, "test", "example", new Class[0], Void.class, new Object[0]);
	private InvokeAtLeastOnceMatcher matcher = new InvokeAtLeastOnceMatcher();

	public void testAlwaysMatches() {
		assertTrue("should match", matcher.matches(emptyInvocation));
		matcher.invoked(emptyInvocation);
		assertTrue("should still match", matcher.matches(emptyInvocation));
	}
	
	public void testVerifyFailsIfNotYetInvoked() {
		try {
			matcher.verify();
		} catch (AssertionFailedError expected) {
			return;
		}
		fail("should have thrown exception");
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
	
	public void testWritesDescriptionOfMatch() {
		String description = matcher.writeTo(new StringBuffer()).toString();
		
		assertEquals( "should describe match", "called at least once", description );
	}
}
