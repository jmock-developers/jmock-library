/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock;

/**
 * A constraint over acceptable values.
 */
public interface Constraint {
    /**
     * Evaluates the constraint for argument <var>o</var>.
     * 
     * @param o 
     *     the object against which the constraint is evaluated.
     * 
     * @return <code>true</code> if <var>o</var> meets the constraint,
     *         <code>false</code> if it does not.
     */
    boolean eval(Object o);
}
