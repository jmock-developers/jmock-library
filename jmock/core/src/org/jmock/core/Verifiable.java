/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core;

/**
 * A <em>Verifiable</em> is an object that can confirm at the end of a unit test that
 * the correct behvaiour has occurred.
 * 
 * @see org.jmock.util.Verifier Verifier to check all the Verifiables in an object.
 */
public interface Verifiable
{

    /**
     * Throw an AssertionFailedException if any expectations have not been met.
     */
    public abstract void verify();
}
