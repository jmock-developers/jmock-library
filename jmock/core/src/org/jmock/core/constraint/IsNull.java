/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;


/**
 * Is the value null?
 */
public class IsNull implements Constraint
{
    public boolean eval( Object o ) {
        return o == null;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("null");
    }
}

