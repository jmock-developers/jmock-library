package org.jmock.core;


/**
 * An object that can describe itself. 
 * Used when reporting a missed expectation in a test case.
 */
public interface SelfDescribing {
    /**
     * Appends the description of this object to the buffer.
     * 
     * @param buffer
     *     The buffer that the description is appended to.
     * @return The buffer passed to the method.
     */
    StringBuffer describeTo( StringBuffer buffer );
}
