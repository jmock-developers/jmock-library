/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core.constraint;

import org.jmock.core.Constraint;

/**
 * A constraint that always returns <code>true</code>.
 */
public class IsAnything implements Constraint {
    public boolean eval(Object o) {
        return true;
    }

    public String toString() {
        return "ANYTHING";
    }
}

