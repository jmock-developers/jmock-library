/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.expectation;

import junit.framework.AssertionFailedError;
import org.jmock.AbstractTestCase;

public class ReturnObjectListTest extends AbstractTestCase {
    private ReturnObjectList list = new ReturnObjectList("test");

    public void testLeftoverObjectFails() {
        list.addObjectToReturn("one");

        assertVerifyFails(list);
    }

    public void testEmptyList() {
        list.verify();
    }

    public void testReturnSucceeds() {
        list.addObjectToReturn("one");
        list.addObjectToReturn("two");

        assertEquals("Should be first result", "one", list.nextReturnObject());
        assertEquals("Should be second result", "two", list.nextReturnObject());
        list.verify();
    }

    public void testTooManyReturns() {
        try {
            list.nextReturnObject();
            fail("Error should have been raised");
        } catch (AssertionFailedError expected) {
        }
    }
}
