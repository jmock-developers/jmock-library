/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamock;

import org.jmock.Constraint;
import org.jmock.constraint.*;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.matcher.AnyArgumentsMatcher;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.NoArgumentsMatcher;


/**
 * Convenient factory functions and constants for building Constraints.
 */
public abstract class C {
    public static final IsAnything IS_ANYTHING = new IsAnything();
    public static final IsNull IS_NULL = new IsNull();
    public static final Constraint IS_NOT_NULL = not(IS_NULL);
    public static final Constraint IS_TRUE = eq(new Boolean(true));
    public static final Constraint IS_FALSE = eq(new Boolean(false));
    public static final Constraint IS_ZERO = eq(new Integer(0));
    public static final Constraint IS_NOT_ZERO = not(IS_ZERO);

    public static final InvocationMatcher NO_ARGS = NoArgumentsMatcher.INSTANCE;
    public static final InvocationMatcher ANY_ARGS = AnyArgumentsMatcher.INSTANCE;

    public static Constraint same(Object o) {
        return new IsSame(o);
    }

    public static Constraint eq(Object o) {
        return new IsEqual(o);
    }

    public static InvocationMatcher eq(Object arg0, Object arg1) {
        return args(eq(arg0), eq(arg1));
    }

    public static InvocationMatcher eq(Object arg0, Object arg1, Object arg2) {
        return args(eq(arg0), eq(arg1), eq(arg2));
    }

    public static InvocationMatcher eq(Object arg0, Object arg1, Object arg2, Object arg3) {
        return args(eq(arg0), eq(arg1), eq(arg2), eq(arg3));
    }

    public static Constraint eq(int n) {
        return new IsEqual(new Integer(n));
    }

    public static Constraint eq(long l) {
        return new IsEqual(new Long(l));
    }

    public static Constraint eq(double d) {
        return new IsEqual(new Double(d));
    }

    public static Constraint gt(int n) {
        return new IsGreaterThan(new Integer(n));
    }

    public static Constraint gt(long l) {
        return new IsGreaterThan(new Long(l));
    }

    public static Constraint gt(double d) {
        return new IsGreaterThan(new Double(d));
    }

    public static Constraint gt(char c) {
        return new IsGreaterThan(new Character(c));
    }

    public static Constraint lt(int n) {
        return new IsLessThan(new Integer(n));
    }

    public static Constraint lt(long l) {
        return new IsLessThan(new Long(l));
    }

    public static Constraint lt(double d) {
        return new IsLessThan(new Double(d));
    }

    public static Constraint lt(char c) {
        return new IsLessThan(new Character(c));
    }

    public static Constraint not(Constraint p) {
        return new IsNot(p);
    }

    public static Constraint and(Constraint p1, Constraint p2) {
        return new And(p1, p2);
    }

    public static Constraint or(Constraint p1, Constraint p2) {
        return new Or(p1, p2);
    }

    public static Constraint isA(Class c) {
        return new IsInstanceOf(c);
    }

    public static Constraint stringContains( String excerpt ) {
    	return new StringContains(excerpt);
    }
    
    /*  Helper methods for succinctly constructing Constraint arrays
     */

    public static InvocationMatcher args() {
        return NO_ARGS;
    }

    public static InvocationMatcher args(Constraint p) {
        return new ArgumentsMatcher(new Constraint[]{p});
    }

    public static InvocationMatcher args(Constraint p1, Constraint p2) {
        return new ArgumentsMatcher(new Constraint[]{p1, p2});
    }

    public static InvocationMatcher args(Constraint p1, Constraint p2, Constraint p3) {
        return new ArgumentsMatcher(new Constraint[]{p1, p2, p3});
    }

    public static InvocationMatcher args(Constraint p1, Constraint p2, Constraint p3, Constraint p4) {
        return new ArgumentsMatcher(new Constraint[]{p1, p2, p3, p4});
    }

    public static InvocationMatcher anyArgs(int argCount) {
        Constraint[] constraints = new Constraint[argCount];
        for (int i = 0; i < constraints.length; i++) {
            constraints[i] = new IsAnything();
        }

        return new ArgumentsMatcher(constraints);
    }
}
