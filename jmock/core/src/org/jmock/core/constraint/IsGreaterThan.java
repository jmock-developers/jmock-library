/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;


/**
 * Is the value greater than another {@link java.lang.Comparable} value?
 */
public class IsGreaterThan implements Constraint
{
    private Comparable lowerLimit;

    public IsGreaterThan( Comparable lowerLimit ) {
        this.lowerLimit = lowerLimit;
    }

    public boolean eval( Object arg ) {
        return lowerLimit.compareTo(arg) < 0;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("a value greater than <")
                .append(lowerLimit)
                .append(">");
    }
}
