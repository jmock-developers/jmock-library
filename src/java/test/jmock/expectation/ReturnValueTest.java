/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.expectation;

import org.jmock.expectation.ReturnValue;

import test.jmock.AbstractTestCase;
import junit.framework.AssertionFailedError;

public class ReturnValueTest extends AbstractTestCase {
    private ReturnValue value;

    protected void setUp() throws Exception {
        super.setUp();
        value = new ReturnValue(getName());
    }

    public void testGetNull() {
        value.setValue(null);
        assertTrue(value.getValue() == null);
    }

    public void testGetValue() {
        value.setValue(this);
        assertEquals(this, value.getValue());
    }

    public void testGetBooleanValue() {
        value.setValue(true);
        assertTrue(value.getBooleanValue());
    }

    public void testIntValue() {
        value.setValue(13);
        assertEquals(13, value.getIntValue());
    }

    public void testLongValue() {
        long now = System.currentTimeMillis();
        value.setValue(now);
        assertEquals(now, value.getLongValue());
        value.getIntValue();
    }

    public void testValueNotSet() {
        try {
            value.getValue();
            fail("Error not thrown");
        } catch (AssertionFailedError e) {
            assertEquals("The return value \"" + getName() + "\" has not been set.", e.getMessage());
        }
    }

}
