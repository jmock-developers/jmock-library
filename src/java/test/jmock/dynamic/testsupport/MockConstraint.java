/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.testsupport;

import junit.framework.Assert;
import org.jmock.Constraint;
import org.jmock.Verifiable;

public class MockConstraint extends Assert implements Constraint, Verifiable {
    private String description;
    private Object expectedArg;
    private boolean result;
    private boolean wasChecked = false;

    public MockConstraint(String description, Object expectedArg, boolean result) {
        this.description = description;
        this.expectedArg = expectedArg;
        this.result = result;
    }

    public String toString() {
        return description;
    }

    public boolean eval(Object arg) {
        assertSame("Should be expected argument", expectedArg, arg);
        wasChecked = true;
        return result;
    }

    public void verify() {
        assertTrue(description + " should have been checked", wasChecked);
    }
}
