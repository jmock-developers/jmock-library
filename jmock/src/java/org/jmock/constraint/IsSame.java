/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import org.jmock.Constraint;

/**
 * Is the value the same object as another value?
 */
public class IsSame implements Constraint {
    private Object _object;

    /**
     * Creates a new instance of IsSame
     * 
     * @param o The predicate evaluates to true only when the argument is
     *          this object.
     */
    public IsSame(Object o) {
        _object = o;
    }

    public boolean eval(Object arg) {
        return arg == _object;
    }

    public String toString() {
        return "the same object as <" + _object + ">";
    }
}
