package test.jmock.core.matcher;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.core.Invocation;
import org.jmock.core.matcher.InvokeAtLeastOnceMatcher;
import org.jmock.expectation.AssertMo;
import test.jmock.core.testsupport.MethodFactory;


public class InvokeAtLeastOnceMatcherTest extends TestCase
{
	private Invocation emptyInvocation;
	private InvokeAtLeastOnceMatcher matcher;

	public void setUp() {
		MethodFactory methodFactory = new MethodFactory();

		emptyInvocation = new Invocation("INVOKED-OBJECT",
		                                 methodFactory.newMethod("example", MethodFactory.NO_ARGUMENTS, Void.class,
		                                                         MethodFactory.NO_EXCEPTIONS),
		                                 new Object[0]);
		matcher = new InvokeAtLeastOnceMatcher();
	}

	public void testAlwaysMatches() {
		assertTrue("should match", matcher.matches(emptyInvocation));
		matcher.invoked(emptyInvocation);
		assertTrue("should still match", matcher.matches(emptyInvocation));
	}

	public void testVerifyFailsIfNotYetInvoked() {
		try {
			matcher.verify();
		}
		catch (AssertionFailedError ex) {
			AssertMo.assertIncludes("should report invokedMethod not invokedObject",
			                        "expected invokedMethod was not invokedObject", ex.getMessage());
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

		assertTrue("should have description", matcher.hasDescription());
		assertEquals("should describe match", MATCH_DESCRIPTION, description);
	}

	public void testReportsWhetherInvokedInDescription() {
		matcher.invoked(emptyInvocation);

		String description = matcher.describeTo(new StringBuffer()).toString();

		assertTrue("should have description", matcher.hasDescription());
		assertTrue("should describe match",
		           description.indexOf(MATCH_DESCRIPTION) >= 0);
		assertTrue("should report has been called",
		           description.indexOf("has been invokedObject") >= 0);
	}
}
