/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;


/**
 * Calculates the logical negation of a constraint.
 */
public class IsNot implements Constraint
{
    private Constraint constraint;

    public IsNot( Constraint constraint ) {
        this.constraint = constraint;
    }

    public boolean eval( Object arg ) {
        return !constraint.eval(arg);
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        buffer.append("not ");
        constraint.describeTo(buffer);
        return buffer;
    }
}
