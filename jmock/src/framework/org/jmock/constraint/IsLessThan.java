/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import org.jmock.Constraint;

/**
 * Is the value less than another {@link java.lang.Comparable} value?
 */
public class IsLessThan implements Constraint {
    private Comparable upperLimit;

    public IsLessThan(Comparable upperLimit) {
        this.upperLimit = upperLimit;
    }

    public boolean eval(Object arg) {
        return upperLimit.compareTo(arg) > 0;
    }

    public String toString() {
        return "a value less than <" + upperLimit + ">";
    }
}
