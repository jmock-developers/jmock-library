/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;

import java.util.Collection;
import java.util.HashSet;


public class ExpectationSet extends AbstractExpectationCollection
{
    private HashSet myExpectedItems = new HashSet();
    private HashSet myActualItems = new HashSet();

    public ExpectationSet( String name ) {
        super(name);
    }

    protected void checkImmediateValues( Object actualItem ) {
        AssertMo.assertTrue(myName + " received an unexpected item\nUnexpected:" + actualItem,
                            new HashSet(myExpectedItems).contains(actualItem));
    }

    protected Collection getActualCollection() {
        return myActualItems;
    }

    protected Collection getExpectedCollection() {
        return myExpectedItems;
    }

    public void verify() {
        assertEquals("did not receive the expected collection items.",
                     new HashSet(getExpectedCollection()),
                     new HashSet(getActualCollection()));
    }

}
