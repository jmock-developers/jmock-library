/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.expectation;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.expectation.AssertMo;
import org.jmock.expectation.ExpectationDoubleValue;


public class ExpectationDoubleValueTest extends TestCase
{

	private ExpectationDoubleValue myExpectation =
	        new ExpectationDoubleValue("ExpectationDoubleValue for testing");

	public void testExpectNothing() {
		myExpectation.setExpectNothing();

		assertTrue("Should have an expectation",
		           myExpectation.hasExpectations());
	}

	public void testExpectNothingFail() {
		myExpectation.setExpectNothing();

		try {
			myExpectation.setActual(100.0);
			fail("Should fail fast");
		}
		catch (AssertionFailedError ex) {
			// expected
		}

	}

	public void testFailOnVerify() {
		myExpectation.setExpected(0.0, 0.0);
		myExpectation.setFailOnVerify();

		myExpectation.setActual(1.0);
		AssertMo.assertVerifyFails(myExpectation);
	}

	public void testFlushActual() {
		myExpectation.setActual(10);

		myExpectation.setExpectNothing();

		myExpectation.verify();
	}

	public void testHasNoExpectations() {
		myExpectation.setActual(0.0);

		assertTrue("Has no expectations",
		           !myExpectation.hasExpectations());
	}

	public void testFailOutsideError() {
		myExpectation.setExpected(100.0, 1.0);

		try {
			myExpectation.setActual(102.0);
			fail("Should fail fast on double");
		}
		catch (AssertionFailedError ex) {
			//expected
		}

	}

	public void testPassOnError() {
		myExpectation.setExpected(100.0, 1.0);
		myExpectation.setActual(101.0);
		myExpectation.verify();
	}

	public void testPassWithinError() {
		myExpectation.setExpected(100.0, 1.0);
		myExpectation.setActual(100);
		myExpectation.verify();
	}
}
