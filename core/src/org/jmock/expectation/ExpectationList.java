/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.expectation;

import java.util.ArrayList;
import java.util.Collection;
import junit.framework.Assert;


public class ExpectationList extends AbstractExpectationCollection
{
    protected ArrayList myExpectedItems = new ArrayList();
    protected ArrayList myActualItems = new ArrayList();

    public ExpectationList( String name ) {
        super(name);
    }

    protected void checkImmediateValues( Object actualItem ) {
        int size = myActualItems.size();
        Assert.assertTrue(myName
                          + " had different sizes\nExpected Size:"
                          + myExpectedItems.size()
                          + "\nReceived size: "
                          + size
                          + " when adding:"
                          + actualItem,
                          myExpectedItems.size() >= size);
        assertEquals(myName + " added item does not match",
                     myExpectedItems.get(size - 1),
                     actualItem);
    }

    protected Collection getActualCollection() {
        return myActualItems;
    }

    protected Collection getExpectedCollection() {
        return myExpectedItems;
    }
}
