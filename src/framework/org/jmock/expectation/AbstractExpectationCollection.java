/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.expectation;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

abstract public class AbstractExpectationCollection extends AbstractExpectation implements ExpectationCollection {

    public AbstractExpectationCollection(String name) {
        super(name);
    }

    public void addActual(Object actualItem) {
        getActualCollection().add(actualItem);
        if (shouldCheckImmediately()) {
            checkImmediateValues(actualItem);
        }
    }

    public void addActual(int actualItem) {
        addActual(new Integer(actualItem));
    }

    public void addActualMany(Object[] items) {
        if (items == null) return;

        for (int i = 0; i < items.length; ++i) {
            addActual(items[i]);
        }
    }

    public void addActualMany(Enumeration items) {
        while (items.hasMoreElements()) {
            addActual(items.nextElement());
        }
    }

    public void addActualMany(Iterator items) {
        while (items.hasNext()) {
            addActual(items.next());
        }
    }

    public void addExpected(int expectedItem) {
        addExpected(new Integer(expectedItem));
    }

    public void addExpected(Object expectedItem) {
        getExpectedCollection().add(expectedItem);
        setHasExpectations();
    }

    public void addExpectedMany(Object[] expectedItems) {
        for (int i = 0; i < expectedItems.length; ++i) {
            addExpected(expectedItems[i]);
        }
        setHasExpectations();
    }

    public void addExpectedMany(Enumeration expectedItems) {
        while (expectedItems.hasMoreElements()) {
            addExpected(expectedItems.nextElement());
        }
        setHasExpectations();
    }

    public void addExpectedMany(Iterator expectedItems) {
        while (expectedItems.hasNext()) {
            addExpected(expectedItems.next());
        }
        setHasExpectations();
    }

    abstract protected void checkImmediateValues(Object actualItem);

    public void clearActual() {
        getActualCollection().clear();
    }

    protected void clearExpectation() {
        getExpectedCollection().clear();
    }

    abstract protected Collection getActualCollection();

    abstract protected Collection getExpectedCollection();

    public void setExpectNothing() {
        clearExpectation();
        setHasExpectations();
    }

    public void verify() {
        assertEquals(
                "did not receive the expected collection items.",
                getExpectedCollection(),
                getActualCollection());
    }

    public void addActual(long actual) {
        addActual(new Long(actual));
    }

    public void addExpected(long expected) {
        addExpected(new Long(expected));
    }
}
