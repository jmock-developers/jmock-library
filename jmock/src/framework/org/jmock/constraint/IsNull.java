/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import org.jmock.Constraint;

/**
 * Is the value null?
 */
public class IsNull implements Constraint {
    public IsNull() {
    }

    public boolean eval(Object o) {
        return o == null;
    }

    public String toString() {
        return "null";
    }
}

