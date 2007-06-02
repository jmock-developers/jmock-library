/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;
import org.jmock.core.Formatting;


/**
 * Is the value the same object as another value?
 */
public class IsSame implements Constraint {
    private Object object;

    /**
     * Creates a new instance of IsSame
     *
     * @param object The predicate evaluates to true only when the argument is
     *               this object.
     */
    public IsSame( Object object ) {
        this.object = object;
    }

    public boolean eval( Object arg ) {
        return arg == object;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("same(")
        	         .append(Formatting.toReadableString(object))
        	         .append(")");
    }
}
