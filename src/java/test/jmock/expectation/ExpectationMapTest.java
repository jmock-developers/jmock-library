/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.expectation;

import org.jmock.expectation.AssertMo;
import org.jmock.expectation.ExpectationMap;

import junit.framework.AssertionFailedError;
import test.jmock.AbstractTestCase;

public class ExpectationMapTest extends AbstractTestCase {

    public void testExpectMissingEntry() {
        ExpectationMap map = new ExpectationMap("map");

        map.addExpectedMissing("key");
        assertEquals("one entry", null, map.get("key"));
        map.verify();
    }

    public void testExpectNullEntry() {

        ExpectationMap map = new ExpectationMap("map");

        try {
            map.addExpected("key", null);
            assertEquals("one entry", null, map.get("key"));
            map.verify();
        } catch (NullPointerException ex) {
            AssertMo.assertStartsWith(
                    "Should be JDK 1.1.7A",
                    "1.1",
                    System.getProperty("java.version"));
        }
    }

    public void testExpectOneEntry() {
        ExpectationMap map = new ExpectationMap("map");

        map.addExpected("key", "value");
        assertEquals("one entry", "value", map.get("key"));
        map.verify();
    }

    public void testExpectTwoEntries() {
        ExpectationMap map = new ExpectationMap("map");

        map.addExpected("key", "value");
        map.addExpected("key1", "value1");

        assertEquals("two entries", "value", map.get("key"));
        assertEquals("two entries", "value1", map.get("key1"));
        map.verify();
    }

    public void testFailOneEntry() {
        try {
            ExpectationMap map = new ExpectationMap("map");
            map.setExpectNothing();
            map.get("key");
        } catch (AssertionFailedError ex) {
            return;
        }
        fail("should fail one entry");
    }

    public void testFailOnVerify() {
        ExpectationMap map = new ExpectationMap("map");
        map.setExpectNothing();
        map.setFailOnVerify();
        map.get("key");

        try {
            map.verify();
        } catch (AssertionFailedError ex) {
            return;
        }
        fail("should fail one entry");
    }

    public void testOverwriteEntry() {
        ExpectationMap map = new ExpectationMap("map");

        map.addExpected("key", "value");
        map.addExpected("key", "value1");

        assertEquals("overwrite entry", "value1", map.get("key"));
        map.verify();
    }
}
