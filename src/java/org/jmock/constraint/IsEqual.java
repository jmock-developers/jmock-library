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
    private Object object;

    public IsEqual(Object equalArg) {
        object = convertArrayToList(equalArg);
    }

    public boolean eval(Object arg) {
        arg = convertArrayToList(arg);
        if (arg == null) {
            return arg == object;
        }
        return arg.equals(object);
    }

    public String toString() {
        return " = " + DynamicUtil.toReadableString(object);
    }
    
    // TODO: get rid of instanceof!
    private Object convertArrayToList(Object equalArg) {
        if (equalArg instanceof Object[]) {
            return Arrays.asList((Object[]) equalArg);
        } else {
            return equalArg;
        }
    }
}
