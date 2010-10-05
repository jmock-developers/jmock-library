/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.testsupport;

import junit.framework.Assert;
import org.jmock.core.Constraint;
import org.jmock.core.Verifiable;


public class MockConstraint extends Assert implements Constraint, Verifiable {
    private String description;
    private Object expectedArg;
    private boolean result;
    private boolean wasChecked = false;


    public MockConstraint( String description ) {
        this(description, null, true);
        wasChecked = true;
    }

    public MockConstraint( String description, Object expectedArg, boolean result ) {
        this.description = description;
        this.expectedArg = expectedArg;
        this.result = result;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append(description);
    }

    public boolean eval( Object arg ) {
        assertSame("Should be expected argument", expectedArg, arg);
        wasChecked = true;
        return result;
    }

    public void verify() {
        assertTrue(description + " should have been checked", wasChecked);
    }
}
