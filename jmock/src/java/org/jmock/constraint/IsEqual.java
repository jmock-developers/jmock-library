/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.constraint;

import org.jmock.Constraint;
import org.jmock.dynamic.DynamicUtil;

import java.util.Arrays;

/**
 * Is the value equal to another value, as tested by the
 * {@link java.lang.Object#equals} method?
 */
public class IsEqual implements Constraint {
    private Object _object;

    public IsEqual(Object equalArg) {
        if (equalArg instanceof Object[]) {
            _object = Arrays.asList((Object[]) equalArg);
        } else {
            _object = equalArg;
        }
    }

    public boolean eval(Object arg) {
        if (arg instanceof Object[]) {
            arg = Arrays.asList((Object[]) arg);
        }
        if (arg == null) {
            return arg == _object;
        }
        return arg.equals(_object);
    }

    public String toString() {
        return " = " + DynamicUtil.toReadableString(_object);
    }

    public boolean equals(Object anObject) {
        return eval(anObject);
    }
}
