/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;


/**
 * An object that can describe itself.
 * Used when reporting a missed expectation in a test case.
 * @since 1.0
 */
public interface SelfDescribing
{
    /**
     * Appends the description of this object to the buffer.
     *
     * @param buffer The buffer that the description is appended to.
     * @return The buffer passed to the invokedMethod.
     */
    StringBuffer describeTo( StringBuffer buffer );
}
