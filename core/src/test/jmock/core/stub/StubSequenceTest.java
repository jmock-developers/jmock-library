package test.jmock.core.stub;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.core.Invocation;
import org.jmock.expectation.AssertMo;
import test.jmock.core.testsupport.MethodFactory;
import test.jmock.core.testsupport.MockStub;


public class StubSequenceTest extends TestCase
{
	private Object invokedObject = new Object()
	{
		public String toString() {
			return "INVOKED_OBJECT";
		}
	};
	private MethodFactory methodFactory = new MethodFactory();
	private Invocation invocation =
	        new Invocation(invokedObject, methodFactory.newMethodReturning(String.class), null);

	static final String RESULT1 = "RESULT1";
	static final String RESULT2 = "RESULT2";
	static final String RESULT3 = "RESULT3";

	public void testInvokesStubsInOrder() throws Throwable {
		MockStub[] stubs = new MockStub[4];

		for (int i = 0; i < stubs.length; i++) {
			stubs[i] = new MockStub();
			stubs[i].invokeResult = "RESULT-" + i;
		}

		StubSequence sequence = new StubSequence(stubs);

		for (int current = 0; current < stubs.length; current++) {
			reset(stubs);

			stubs[current].invokeInvocation.setExpected(invocation);

			assertEquals("should be result of stubs[" + current + "]",
			             stubs[current].invokeResult, sequence.invoke(invocation));

			stubs[current].verify();
		}
	}

	public void testThrowsAssertionFailedErrorIfInvokedMoreTimesThanThereAreStubs() throws Throwable {
		MockStub[] stubs = new MockStub[]{new MockStub(), new MockStub()};
		StubSequence sequence = new StubSequence(stubs);

		for (int i = 0; i < stubs.length; i++) sequence.invoke(invocation);

		try {
			sequence.invoke(invocation);
		}
		catch (AssertionFailedError ex) {
			AssertMo.assertIncludes("should describe error",
			                        "no more stubs", ex.getMessage());
		}
	}

	public void testDescribesItselfAsSequenceOfStubs() throws Throwable {
		MockStub[] stubs = new MockStub[]{new MockStub(), new MockStub()};

		stubs[0].describeToOutput = "STUB-0";
		stubs[1].describeToOutput = "STUB-1";

		StubSequence sequence = new StubSequence(stubs);

		String sequenceDescription = sequence.describeTo(new StringBuffer()).toString();

		for (int i = 0; i < stubs.length; i++) {
			AssertMo.assertIncludes("should include stub " + i,
			                        stubs[i].describeToOutput, sequenceDescription);

			if (i > 0) {
				int h = i - 1;

				assertTrue("description of stub " + h + " should be before that of stub " + i,
				           sequenceDescription.indexOf(stubs[h].describeToOutput) <
				           sequenceDescription.indexOf(stubs[i].describeToOutput));
			}
		}
	}

	private void reset( MockStub[] stubs ) {
		for (int i = 0; i < stubs.length; i++) {
			stubs[i].invokeInvocation.setExpectNothing();
		}
	}
}
