/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;


/**
 * Calculates the logical conjunction of two constraints. Evaluation is
 * shortcut, so that the second constraint is not called if the first
 * constraint returns <code>false</code>.
 */
public class And implements Constraint
{
    Constraint left, right;

    public And( Constraint left, Constraint right ) {
        this.left = left;
        this.right = right;
    }

    public boolean eval( Object o ) {
        return left.eval(o) && right.eval(o);
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        buffer.append("(");
        left.describeTo(buffer);
        buffer.append(" and ");
        right.describeTo(buffer);
        buffer.append(")");
        return buffer;
    }
}
