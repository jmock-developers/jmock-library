/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.core.stub;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.core.Invocation;
import org.jmock.core.stub.ReturnStub;
import org.jmock.expectation.AssertMo;
import test.jmock.core.testsupport.MethodFactory;


public class ReturnStubTest
        extends TestCase
{
	static final String RESULT = "result";

	MethodFactory methodFactory;
	Object invokedObject;
	Class invokedObjectClass;
	Invocation invocation;
	ReturnStub returnStub;

	public void setUp() {
		methodFactory = new MethodFactory();

		invokedObject = "INVOKED-OBJECT";
		invokedObjectClass = Void.class;

		returnStub = new ReturnStub(RESULT);
	}

	public void testReturnsValuePassedToConstructor() throws Throwable {
		invocation = new Invocation(invokedObject, methodFactory.newMethodReturning(RESULT.getClass()), null);

		assertSame("Should be the same result object",
		           RESULT, returnStub.invoke(invocation));
	}

	public void testIncludesValueInDescription() {
		StringBuffer buffer = new StringBuffer();

		returnStub.describeTo(buffer);

		String description = buffer.toString();

		assertTrue("contains result in description",
		           description.indexOf(RESULT.toString()) >= 0);
		assertTrue("contains 'returns' in description",
		           description.indexOf("returns") >= 0);
	}

	public void testThrowsAssertionFailedErrorIfTriesToReturnValueOfIncompatibleType()
	        throws Throwable {
		invocation = new Invocation(invokedObject, methodFactory.newMethodReturning(int.class), null);

		try {
			returnStub.invoke(invocation);
		}
		catch (AssertionFailedError ex) {
			AssertMo.assertIncludes("expected return type", invocation.invokedMethod.getReturnType().toString(), ex.getMessage());
			AssertMo.assertIncludes("returned value type", RESULT.getClass().toString(), ex.getMessage());
			return;
		}
		fail("should have failed");
	}

	public void testThrowsAssertionFailedErrorIfTriesToReturnValueFromVoidMethod()
	        throws Throwable {
		invocation = new Invocation(invokedObject, methodFactory.newMethodReturning(void.class), null);

		try {
			returnStub.invoke(invocation);
		}
		catch (AssertionFailedError ex) {
			AssertMo.assertIncludes("should describe error",
			                        "tried to return a value from a void method", ex.getMessage());
			return;
		}
		fail("should have failed");
	}

	public void testCanReturnNullReference()
	        throws Throwable {
		invocation = new Invocation(invokedObject, methodFactory.newMethodReturning(String.class), null);

		returnStub = new ReturnStub(null);

		assertNull("should return null", returnStub.invoke(invocation));
	}

	public void testThrowsAssertionFailedErrorIfTriesToReturnNullFromMethodWithPrimitiveReturnType()
	        throws Throwable {
		invocation = new Invocation(invokedObject, methodFactory.newMethodReturning(int.class), null);

		returnStub = new ReturnStub(null);

		try {
			returnStub.invoke(invocation);
		}
		catch (AssertionFailedError ex) {
			AssertMo.assertIncludes("expected return type", invocation.invokedMethod.getReturnType().toString(), ex.getMessage());
			AssertMo.assertIncludes("null", String.valueOf((Object)null), ex.getMessage());
			return;
		}
		fail("should have failed");
	}
}
