/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.expectation;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.expectation.AssertMo;
import org.jmock.expectation.ExpectationSegment;


public class ExpectationSegmentTest extends TestCase
{

	private ExpectationSegment myExpectation;

	public void setUp() {
		myExpectation = new ExpectationSegment("Expectation segment");
	}

	public void testExpectNothing() {
		myExpectation.setExpectNothing();

		assertTrue("Should have an expectation", myExpectation.hasExpectations());
	}

	public void testExpectNothingFail() {
		myExpectation.setExpectNothing();

		boolean hasThrownException = false;
		try {
			myExpectation.setActual("some string");
		}
		catch (AssertionFailedError ex) {
			hasThrownException = true;
		}

		assertTrue("Should fail fast", hasThrownException);
	}

	public void testFailOnVerify() {
		myExpectation.setExpected("a segment");
		myExpectation.setFailOnVerify();

		myExpectation.setActual("string without stuff");
		AssertMo.assertVerifyFails(myExpectation);
	}

	public void testFailsImmediately() {

		boolean hasThrownException = false;
		myExpectation.setExpected("inner");
		try {
			myExpectation.setActual("String not containing segment");
		}
		catch (AssertionFailedError expected) {
			hasThrownException = true;
		}

		assertTrue("Should have thrown exception", hasThrownException);
	}

	public void testFlushActual() {
		myExpectation.setActual("a string");

		myExpectation.setExpectNothing();

		myExpectation.verify();
	}

	public void testHasNoExpectations() {
		myExpectation.setActual("a string");

		assertTrue("Has no expectations", !myExpectation.hasExpectations());
	}

	public void testPasses() {

		myExpectation.setExpected("inner");
		myExpectation.setActual("String containing inner segment");

		myExpectation.verify();
	}
}
