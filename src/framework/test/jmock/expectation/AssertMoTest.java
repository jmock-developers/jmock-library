/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.expectation;

import java.util.Vector;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.expectation.AssertMo;

public class AssertMoTest extends TestCase {

    public void testAssertExcludes() {
        AssertMo.assertExcludes(
                "Should not include substring",
                "dog",
                "The quick brown fox");
    }

    public void testAssertExcludesFails() {
        Throwable result = null;
        try {
            AssertMo.assertExcludes(
                    "Should fail on exclude",
                    "fox",
                    "The quick brown fox");
        } catch (AssertionFailedError ex) {
            result = ex;
        }

        assertTrue("Should get an exception", result != null);

    }

    public void testAssertIncludes() {
        AssertMo.assertIncludes(
                "Should include a substring",
                "fox",
                "The quick brown fox");
    }

    public void testAssertIncludesFails() {
        Throwable result = null;
        try {
            AssertMo.assertIncludes(
                    "Should fail if no include",
                    "dog",
                    "The quick brown fox");
        } catch (AssertionFailedError ex) {
            result = ex;
        }

        assertTrue("Should get an exception", result != null);

    }

    public void testAssertStartsWith() {
        AssertMo.assertStartsWith(
                "Should start with fox",
                "fox",
                "fox quick brown");
    }

    public void testAssertStartsWithFails() {
        Throwable result = null;
        try {
            AssertMo.assertStartsWith(
                    "Should fail if it doesn't start with fox",
                    "fox",
                    "The quick brown fox");
        } catch (AssertionFailedError ex) {
            result = ex;
        }

        assertTrue("Should get an exception", result != null);

    }

    public void testDifferentArrays() {
        Object[] anExpectedArray = new Object[]{"one", new Integer(2)};
        Object[] anActualArray = new Object[]{"two", new Integer(2)};

        boolean threwException = false;
        try {
            AssertMo.assertEquals(
                    "Should be expected value",
                    anExpectedArray,
                    anActualArray);
        } catch (AssertionFailedError ignoredException) {
            threwException = true;
        }
        assertTrue("should have thrown assertion failure", threwException);
    }

    public void testDifferentLengthArrays() {
        Object[] anExpectedArray = new Object[]{"one", new Integer(2)};
        Object[] anActualArray = new Object[]{"one"};

        boolean threwException = false;
        try {
            AssertMo.assertEquals(
                    "Should be expected value",
                    anExpectedArray,
                    anActualArray);
        } catch (AssertionFailedError ignoredException) {
            threwException = true;
        }
        assertTrue("should have thrown assertion failure", threwException);
    }

    public void testDifferentObjectArrays() {
        Object[] anExpectedArray = new Object[]{"one", new Integer(2)};
        Object[] anActualArray = new Object[]{new Integer(2), new Vector()};

        boolean threwException = false;
        try {
            AssertMo.assertEquals(
                    "Should be expected value",
                    anExpectedArray,
                    anActualArray);
        } catch (AssertionFailedError ignoredException) {
            threwException = true;
        }
        assertTrue("should have thrown assertion failure", threwException);
    }

    public void testEqualArrays() {
        Object[] anExpectedArray = new Object[]{"one", new Integer(2)};
        Object[] anActualArray = new Object[]{"one", new Integer(2)};

        AssertMo.assertEquals(
                "Should be expected value",
                anExpectedArray,
                anActualArray);
    }

    public void testEqualEmptyArrays() {
        Object[] anExpectedArray = new Object[0];
        Object[] anActualArray = new Object[0];

        AssertMo.assertEquals(
                "Should be expected value",
                anExpectedArray,
                anActualArray);
    }

    public void testFailureCheckerWithFailure() {
        AssertMo.assertFails("Test Description",
                new Runnable() {
                    public void run() {
                        fail("Should not be propagated");
                    }
                });
    }

    public void testFailureCheckerWithoutFailure() {
        final String TEST_MESSAGE = "Test Description";
        try {
            AssertMo.assertFails(TEST_MESSAGE, new Runnable() {
                public void run() {
                }
            });
        } catch (AssertionFailedError expected) {
            assertEquals(TEST_MESSAGE, expected.getMessage());
            return;
        }
        fail("Should have thrown an exception");
    }

}
