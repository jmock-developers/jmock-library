/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;


/**
 * Tests whether the value is an instance of a class.
 */
public class IsInstanceOf implements Constraint
{
    private Class theClass;

    /**
     * Creates a new instance of IsInstanceOf
     *
     * @param theClass The predicate evaluates to true for instances of this class
     *                 or one of its subclasses.
     */
    public IsInstanceOf( Class theClass ) {
        this.theClass = theClass;
    }

    public boolean eval( Object arg ) {
        return theClass.isInstance(arg);
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("an instance of ")
                .append(theClass.getName());
    }
}
