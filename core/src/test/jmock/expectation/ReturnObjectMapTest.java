/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.expectation;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.expectation.AssertMo;
import org.jmock.expectation.ReturnObjectMap;


public class ReturnObjectMapTest extends TestCase
{
    private ReturnObjectMap map;
    private static final String KEY1 = "key1";
    private static final String KEY2 = "key2";
    private static final short SHORT_KEY1 = 1;
    private static final short SHORT_KEY2 = 2;
    private static final String VALUE_ONE = "one";
    private static final String VALUE_TWO = "two";

    protected void setUp() throws Exception {
        super.setUp();
        map = new ReturnObjectMap(getName());
    }

    public void testLeftoverObjectFails() {
        map.putReturnValue(KEY1, VALUE_ONE);

        AssertMo.assertVerifyFails(map);
    }

    public void testEmptyList() {
        map.verify();
    }

    public void testReturnSucceeds() {
        map.putReturnValue(KEY1, VALUE_ONE);
        map.putReturnValue(KEY2, VALUE_TWO);

        assertEquals("Should be first result", VALUE_ONE, map.getValue(KEY1));
        assertEquals("Should be second result", VALUE_TWO, map.getValue(KEY2));
        map.verify();
    }

    public void testReturnInt() {
        map.putReturnValue(KEY1, 1);

        assertEquals("Should be 1", 1, map.getIntValue(KEY1));
        map.verify();
    }

    public void testReturnBoolean() {
        map.putReturnValue(KEY1, true);

        assertEquals("Should be true", true, map.getBooleanValue(KEY1));
        map.verify();
    }

    public void testShortKey() {
        map.putReturnValue(SHORT_KEY1, VALUE_ONE);
        map.putReturnValue(SHORT_KEY2, VALUE_TWO);

        assertEquals("Should be first result", VALUE_ONE, map.getValue(SHORT_KEY1));
        assertEquals("Should be second result", VALUE_TWO, map.getValue(SHORT_KEY2));
        map.verify();
    }

    public void testNoListForKey() {
        try {
            map.getValue(KEY1);
            fail("AssertionFiledError not thrown");
        }
        catch (AssertionFailedError e) {
            assertEquals(getName() + " does not contain key1", e.getMessage());
        }
    }

    public void testNullKey() {
        map.putReturnValue(null, VALUE_ONE);
        assertEquals(VALUE_ONE, map.getValue(null));
    }

    public void testManyReturns() {
        map.putReturnValue(KEY1, VALUE_ONE);
        assertEquals(map.getValue(KEY1), map.getValue(KEY1));
    }

}
