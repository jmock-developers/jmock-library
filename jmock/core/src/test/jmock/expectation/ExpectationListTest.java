/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.expectation;

import org.jmock.expectation.ExpectationList;


public class ExpectationListTest extends AbstractTestExpectationCollection
{

    protected void setUp() throws Exception {
        super.setUp();
        myExpectation = new ExpectationList(getClass().getName());
    }

    // see super-class for tests

    public void testSorted() {
        myExpectation.addExpected("A");
        myExpectation.addExpected("B");

        myExpectation.addActual("A");
        myExpectation.addActual("B");

        myExpectation.verify();
    }
}
