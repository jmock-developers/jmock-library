/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.expectation;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.expectation.AssertMo;
import org.jmock.expectation.ExpectationValue;

public class ExpectationValueTest extends TestCase {

    private ExpectationValue myExpectation = new ExpectationValue("ExpectationValue for testing");

    public void testBooleanFail() {
        myExpectation.setExpected(true);

        boolean testPasses = false;
        try {
            myExpectation.setActual(false);
        } catch (AssertionFailedError ex) {
            testPasses = true;
        }

        assertTrue("Should fail fast on boolean", testPasses);
    }

    public void testBooleanPass() {
        myExpectation.setExpected(true);

        myExpectation.setActual(true);

        myExpectation.verify();
    }

    public void testExpectNothing() {
        myExpectation.setExpectNothing();

        assertTrue("Should have an expectation", myExpectation.hasExpectations());
    }

    public void testExpectNothingFail() {
        myExpectation.setExpectNothing();

        boolean testPasses = false;
        try {
            myExpectation.setActual("another object");
        } catch (AssertionFailedError ex) {
            testPasses = true;
        }

        assertTrue("Should fail fast on object", testPasses);
    }

    public void testFailOnVerify() {
        myExpectation.setExpected("string object");
        myExpectation.setFailOnVerify();

        myExpectation.setActual("another object");
        AssertMo.assertVerifyFails(myExpectation);
    }

    public void testFlushActual() {
        myExpectation.setActual(10);

        myExpectation.setExpectNothing();

        myExpectation.verify();
    }

    public void testHasNoExpectations() {
        myExpectation.setActual("a value");

        assertTrue("Has no expectations", !myExpectation.hasExpectations());
    }

    public void testIntFail() {
        myExpectation.setExpected(100);

        boolean testPasses = false;
        try {
            myExpectation.setActual(150);
        } catch (AssertionFailedError ex) {
            testPasses = true;
        }

        assertTrue("Should fail fast on int", testPasses);
    }

    public void testIntPass() {
        myExpectation.setExpected(100);

        myExpectation.setActual(100);

        myExpectation.verify();
    }

    public void testLongFail() {
        myExpectation.setExpected(100L);

        boolean testPasses = false;
        try {
            myExpectation.setActual(150L);
        } catch (AssertionFailedError ex) {
            testPasses = true;
        }

        assertTrue("Should fail fast on long", testPasses);
    }

    public void testLongPass() {
        myExpectation.setExpected(100L);

        myExpectation.setActual(100L);

        myExpectation.verify();
    }

    public void testDoubleFail() {
        myExpectation.setExpected(100.0);

        boolean testPasses = false;
        try {
            myExpectation.setActual(150.0);
        } catch (AssertionFailedError ex) {
            testPasses = true;
        }

        assertTrue("Should fail fast on double", testPasses);
    }

    public void testDoublePass() {
        myExpectation.setExpected(100.0);

        myExpectation.setActual(100.0);

        myExpectation.verify();
    }

    public void testNullFail() {
        myExpectation.setExpected(null);

        boolean testPasses = false;
        try {
            myExpectation.setActual("another object");
        } catch (AssertionFailedError ex) {
            testPasses = true;
        }

        assertTrue("Should fail fast on object", testPasses);
    }

    public void testNullPass() {
        myExpectation.setExpected(null);
        myExpectation.setActual(null);
        myExpectation.verify();
    }

    public void testObject() {
        myExpectation.setExpected("string object");

        myExpectation.setActual("string object");

        myExpectation.verify();
    }

    public void testObjectFail() {
        myExpectation.setExpected("string object");

        boolean testPasses = false;
        try {
            myExpectation.setActual("another object");
        } catch (AssertionFailedError ex) {
            testPasses = true;
        }

        assertTrue("Should fail fast on object", testPasses);
    }
}
