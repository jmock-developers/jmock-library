/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.expectation;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.expectation.AssertMo;
import org.jmock.expectation.ReturnObjectBag;

public class ReturnObjectBagTest extends TestCase {
    private ReturnObjectBag bag;
    private static final String KEY1 = "key1";
    private static final String KEY2 = "key2";
    private static final short SHORT_KEY1 = 1;
    private static final short SHORT_KEY2 = 2;
    private static final String VALUE_ONE = "one";
    private static final String VALUE_TWO = "two";

    protected void setUp() throws Exception {
        super.setUp();
        bag = new ReturnObjectBag(getName());
    }

    public void testLeftoverObjectFails() {
        bag.putObjectToReturn(KEY1, VALUE_ONE);

        AssertMo.assertVerifyFails(bag);
    }

    public void testEmptyList() {
        bag.verify();
    }

    public void testReturnSucceeds() {
        bag.putObjectToReturn(KEY1, VALUE_ONE);
        bag.putObjectToReturn(KEY2, VALUE_TWO);

        assertEquals("Should be first result", VALUE_ONE, bag.getNextReturnObject(KEY1));
        assertEquals("Should be second result", VALUE_TWO, bag.getNextReturnObject(KEY2));
        bag.verify();
    }

    public void testReturnInt() {
        bag.putObjectToReturn(KEY1, 1);

        assertEquals("Should be 1", 1, bag.getNextReturnInt(KEY1));
        bag.verify();
    }

    public void testReturnBoolean() {
        bag.putObjectToReturn(KEY1, true);

        assertEquals("Should be true", true, bag.getNextReturnBoolean(KEY1));
        bag.verify();
    }

    public void testShortKey() {
        bag.putObjectToReturn(SHORT_KEY1, VALUE_ONE);
        bag.putObjectToReturn(SHORT_KEY2, VALUE_TWO);

        assertEquals("Should be first result", VALUE_ONE, bag.getNextReturnObject(SHORT_KEY1));
        assertEquals("Should be second result", VALUE_TWO, bag.getNextReturnObject(SHORT_KEY2));
        bag.verify();
    }

    public void testNoListForKey() {
        try {
            bag.getNextReturnObject(KEY1);
            fail("AssertionFiledError not thrown");
        } catch (AssertionFailedError e) {
            assertEquals(getName() + " does not contain key1", e.getMessage());
        }
    }

    public void testNullKey() {
        bag.putObjectToReturn(null, VALUE_ONE);
        assertEquals(VALUE_ONE, bag.getNextReturnObject(null));
    }

    public void testTooManyReturns() {
        bag.putObjectToReturn(KEY1, VALUE_ONE);
        bag.getNextReturnObject(KEY1);
        try {
            bag.getNextReturnObject(KEY1);
            fail("AssertionFiledError not thrown");
        } catch (AssertionFailedError e) {
            assertEquals(getName() + ".key1 has run out of objects.", e.getMessage());
        }
    }
}
