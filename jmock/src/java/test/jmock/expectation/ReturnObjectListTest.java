/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.expectation;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.expectation.AssertMo;
import org.jmock.expectation.ReturnObjectList;

public class ReturnObjectListTest extends TestCase {
    private ReturnObjectList list = new ReturnObjectList("test");

    public void testLeftoverObjectFails() {
        list.addObjectToReturn("one");

        AssertMo.assertVerifyFails(list);
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
