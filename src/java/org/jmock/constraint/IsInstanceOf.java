/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import org.jmock.Constraint;

/**
 * Tests whether the value is an instance of a class.
 */
public class IsInstanceOf implements Constraint {
    private Class _class;

    /**
     * Creates a new instance of IsInstanceOf
     * 
     * @param theclass The predicate evaluates to true for instances of this class
     *                 or one of its subclasses.
     */
    public IsInstanceOf(Class theclass) {
        _class = theclass;
    }

    public boolean eval(Object arg) {
        return _class.isInstance(arg);
    }

    public String toString() {
        return "an instance of <" + _class.getName() + ">";
    }
}
