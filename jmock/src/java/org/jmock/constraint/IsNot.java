/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import org.jmock.Constraint;

/**
 * Calculates the logical negation of a constraint.
 */
public class IsNot implements Constraint {
    private Constraint _predicate;

    public IsNot(Constraint p) {
        _predicate = p;
    }

    public boolean eval(Object arg) {
        return !_predicate.eval(arg);
    }

    public String toString() {
        return "not " + _predicate;
    }
}
