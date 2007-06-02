/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

/**
 * A constraint over acceptable values.
 * @since 1.0
 */
public interface Constraint extends SelfDescribing
{
    /**
     * Evaluates the constraint for argument <var>o</var>.
     *
     * @param o the object against which the constraint is evaluated.
     * @return <code>true</code> if <var>o</var> meets the constraint,
     *         <code>false</code> if it does not.
     */
    boolean eval( Object o );
}
