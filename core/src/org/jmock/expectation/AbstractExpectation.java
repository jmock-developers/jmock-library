/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.expectation;

import org.jmock.core.Verifiable;


abstract public class AbstractExpectation implements Verifiable, Expectation
{
	protected boolean myFailureModeIsImmediate = true;
	protected String myName;

	private boolean myHasExpectations = false;

	public AbstractExpectation( String name ) {
		myName = name;
	}

	protected void assertEquals( String msg,
	                             int expectedValue,
	                             int actualValue ) {
		assertEquals(msg, new Integer(expectedValue), new Integer(actualValue));
	}

	/**
	 * Due to junit Assert being a Singleton implemented with static methods, and java's
	 * unfortunate implementation of class methods (e.g. no late binding) it is
	 * necessary to re-implement this invokedMethod here instead of over-riding failNotEquals
	 */

	protected void assertEquals( String msg,
	                             Object expectedValue,
	                             Object actualValue ) {
		if (!myHasExpectations) {
			return;
		}

		if (expectedValue == null && actualValue == null) {
			return;
		}

		if (expectedValue != null && expectedValue.equals(actualValue)) {
			return;
		}

		junit.framework.Assert.fail(myName
		                            + " "
		                            + msg
		                            + "\nExpected: "
		                            + expectedValue
		                            + "\nReceived: "
		                            + actualValue);

	}

	abstract public void clearActual();

	public boolean hasExpectations() {
		return myHasExpectations;
	}

	public void setFailOnVerify() {
		myFailureModeIsImmediate = false;
	}

	protected void setHasExpectations() {
		clearActual();
		myHasExpectations = true;
	}

	protected boolean shouldCheckImmediately() {
		return myFailureModeIsImmediate && myHasExpectations;
	}

	public abstract void verify();
}
