/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.expectation;

import java.util.Vector;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.expectation.AssertMo;
import org.jmock.expectation.ExpectationCollection;


public abstract class AbstractTestExpectationCollection extends TestCase
{
	ExpectationCollection myExpectation;

	public void testEmpty() {
		myExpectation.verify();
	}

	public void testFailImmediately() {
		myExpectation.addExpected("A");
		myExpectation.addExpected("B");

		myExpectation.addActual("A");
		try {
			myExpectation.addActual("C");
		}
		catch (AssertionFailedError ex) {
			return;
		}
		fail("Should have failed immediately");
	}

	public void testFailImmediatelyAddingTooMany() {
		myExpectation.addExpected("A");

		myExpectation.addActual("A");
		try {
			myExpectation.addActual("C");
		}
		catch (AssertionFailedError ex) {
			return;
		}
		fail("Should have failed immediately");
	}

	public void testFailOnSizes() {
		myExpectation.addExpected("A");
		myExpectation.addExpected("B");

		myExpectation.addActual("A");
		myExpectation.addActual("B");

		try {
			myExpectation.addActual("C");
		}
		catch (AssertionFailedError ex) {
			return;
		}
		fail("Should have failed immediately");
	}

	public void testFailOnVerify() {
		myExpectation.setFailOnVerify();
		myExpectation.addExpectedMany(new String[]{"A", "B"});

		myExpectation.addActualMany(new String[]{"C", "A"});

		AssertMo.assertVerifyFails(myExpectation);
	}

	public void testFlushActual() {

		myExpectation.addActual("a value");

		myExpectation.setExpectNothing();
		myExpectation.verify();
	}

	public void testHasExpectations() {
		assertTrue("Should not have any expectations",
		           !myExpectation.hasExpectations());

		myExpectation.addExpected("item");

		assertTrue("Should have an expectation", myExpectation.hasExpectations());
	}

	public void testHasExpectationsForAddingManyArray() {
		assertTrue("Should not have any expectations",
		           !myExpectation.hasExpectations());

		myExpectation.addExpectedMany(new Object[0]);

		assertTrue("Should have an expectation", myExpectation.hasExpectations());
	}

	public void testHasExpectationsForAddingManyVector() {
		assertTrue("Should not have any expectations",
		           !myExpectation.hasExpectations());

		myExpectation.addExpectedMany(new Vector().elements());

		assertTrue("Should have an expectation", myExpectation.hasExpectations());
	}

	public void testHasNoExpectations() {

		myExpectation.addActual("a value");
		assertTrue("Has no expectations", !myExpectation.hasExpectations());
	}

	public void testManyFromEnumeration() {
		Vector expectedItems = new Vector();
		expectedItems.addElement("A");
		expectedItems.addElement("B");

		Vector actualItems = (Vector)expectedItems.clone();

		myExpectation.addExpectedMany(expectedItems.elements());

		myExpectation.addActualMany(actualItems.elements());

		myExpectation.verify();
	}

	public void testManyFromIterator() {
		Vector expectedItems = new Vector();
		expectedItems.addElement("A");
		expectedItems.addElement("B");

		Vector actualItems = (Vector)expectedItems.clone();

		myExpectation.addExpectedMany(expectedItems.iterator());

		myExpectation.addActualMany(actualItems.iterator());

		myExpectation.verify();
	}

	public void testMultiFailureFromEnumeration() {
		Vector expectedItems = new Vector();
		expectedItems.addElement("A");
		expectedItems.addElement("B");

		Vector actualItems = new Vector();
		actualItems.addElement("A");
		actualItems.addElement("C");

		myExpectation.addExpectedMany(expectedItems.elements());
		myExpectation.setFailOnVerify();

		myExpectation.addActualMany(actualItems.elements());

		AssertMo.assertVerifyFails(myExpectation);
	}

	public void testMultiFailureFromIterator() {
		Vector expectedItems = new Vector();
		expectedItems.addElement("A");
		expectedItems.addElement("B");

		Vector actualItems = new Vector();
		actualItems.addElement("A");
		actualItems.addElement("C");

		myExpectation.addExpectedMany(expectedItems.iterator());
		myExpectation.setFailOnVerify();

		myExpectation.addActualMany(actualItems.iterator());

		AssertMo.assertVerifyFails(myExpectation);
	}

	public void testMultiFailureSizes() {
		myExpectation.addExpectedMany(new String[]{"A", "B"});
		myExpectation.setFailOnVerify();

		myExpectation.addActualMany(new String[]{"A", "B", "C"});

		AssertMo.assertVerifyFails(myExpectation);
	}

	public void testExpectingALong() {
		final long expectedLong = 666l;

		myExpectation.addExpected(expectedLong);
		myExpectation.addActual(expectedLong);

		myExpectation.verify();
	}
}
