/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import org.jmock.Constraint;

/**
 * Tests whether the value is an instance of a class.
 */
public class IsInstanceOf implements Constraint {
    private Class theClass;

    /**
     * Creates a new instance of IsInstanceOf
     * 
     * @param theclass The predicate evaluates to true for instances of this class
     *                 or one of its subclasses.
     */
    public IsInstanceOf(Class theClass) {
        this.theClass = theClass;
    }

    public boolean eval(Object arg) {
        return theClass.isInstance(arg);
    }

    public String toString() {
        return "an instance of " + theClass.getName();
    }
}
