/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import org.jmock.Constraint;

/**
 * Is the value less than another {@link java.lang.Comparable} value?
 */
public class IsLessThan implements Constraint {
    private Comparable _object;

    public IsLessThan(Comparable o) {
        _object = o;
    }

    public boolean eval(Object arg) {
        return _object.compareTo(arg) > 0;
    }

    public String toString() {
        return "a value less than <" + _object + ">";
    }
}
