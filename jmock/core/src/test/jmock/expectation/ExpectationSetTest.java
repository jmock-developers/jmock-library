/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.expectation;

import java.util.Vector;
import org.jmock.expectation.ExpectationSet;
import org.jmock.expectation.MapEntry;


public class ExpectationSetTest extends AbstractTestExpectationCollection
{

    protected void setUp() throws Exception {
        super.setUp();
        myExpectation = new ExpectationSet(getClass().getName());
    }

    // look at super-class for more tests.

    public void testMultiUnsorted() {
        myExpectation.addExpectedMany(new String[]{"A", "B"});

        myExpectation.addActualMany(new String[]{"A", "B"});

        myExpectation.verify();
    }

    public void testChangingHashcode() {
        final Vector value = new Vector();

        myExpectation.addExpected(new MapEntry("key", value));
        myExpectation.addActual(new MapEntry("key", value));

        value.add(getName());

        myExpectation.verify();
    }

    public void testChanginHashcodeImediateCheck() {
        final Vector value = new Vector();

        myExpectation.addExpected(new MapEntry("key", value));
        value.add(getName());
        myExpectation.addActual(new MapEntry("key", value));

        myExpectation.verify();
    }

    public void testMultiUnsortedSet() {
        myExpectation.addExpectedMany(new String[]{"A", "B"});

        myExpectation.addActualMany(new String[]{"A", "B", "A", "B"});

        myExpectation.verify();
    }

    public void testUnsorted() {
        myExpectation.addExpected("A");
        myExpectation.addExpected("B");

        myExpectation.addActual("B");
        myExpectation.addActual("A");

        myExpectation.verify();
    }

    public void testUnsortedSet() {
        myExpectation.addExpected("A");
        myExpectation.addExpected("B");

        myExpectation.addActual("A");
        myExpectation.addActual("B");
        myExpectation.addActual("A");
        myExpectation.addActual("B");

        myExpectation.verify();
    }
}
