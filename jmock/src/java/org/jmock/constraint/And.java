/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import org.jmock.Constraint;

/**
 * Calculates the logical conjunction of two constraints.
 * Evaluation is shortcut, so that the second constraint is not
 * called if the first constraint returns <code>false</code>.
 */
public class And
        implements Constraint {
    Constraint _p1, _p2;

    public And(Constraint p1, Constraint p2) {
        _p1 = p1;
        _p2 = p2;
    }

    public boolean eval(Object o) {
        return _p1.eval(o) && _p2.eval(o);
    }

    public String toString() {
        return "(" + _p1 + " and " + _p2 + ")";
    }
}
