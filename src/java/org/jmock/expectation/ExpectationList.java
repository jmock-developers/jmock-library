/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.expectation;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Collection;

public class ExpectationList extends AbstractExpectationCollection {
    protected ArrayList myExpectedItems = new ArrayList();
    protected ArrayList myActualItems = new ArrayList();

    public ExpectationList(String name) {
        super(name);
    }

    protected void checkImmediateValues(Object actualItem) {
        int size = myActualItems.size();
        Assert.assertTrue(
                myName
                + " had different sizes\nExpected Size:"
                + myExpectedItems.size()
                + "\nReceived size: "
                + size
                + " when adding:"
                + actualItem,
                myExpectedItems.size() >= size);
        assertEquals(
                myName + " added item does not match",
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
