/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.expectation;

import org.jmock.core.Verifiable;

/**
 * An <EM>Expectation</EM> is an object that we set up at the beginning of a unit test to
 * expect certain things to happen to it. If it is possible to tell, the Expectation will
 * fail as soon as an incorrect value has been set.
 * <p/>
 * Call verify() at the end of a unit test to check for missing or incomplete values.
 * <p/>
 * If no expectations have been set on the object, then no checking will be done and
 * verify() will do nothing.
 */
public interface Expectation extends Verifiable {

    /**
     * Return true if any expectations have been set on this object.
     */
    public boolean hasExpectations();

    /**
     * Tell the object to expect nothing to happen to it, perhaps because the test is exercising
     * the handling of an error. The Expectation will fail if any actual values are set.
     * <p/>
     * Note that this is not the same as not setting any expectations, in which case verify()
     * will do nothing.
     */
    void setExpectNothing();

    /**
     * If an incorrect actual value is set, defer reporting this as a failure until verify()
     * is called on this object.
     */
    public void setFailOnVerify();
}
