/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;


/**
 * A constraint that always returns <code>true</code>.
 */
public class IsAnything implements Constraint
{
    public boolean eval( Object o ) {
        return true;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("ANYTHING");
    }
}

