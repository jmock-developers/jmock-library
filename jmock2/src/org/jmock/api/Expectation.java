package org.jmock.api;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

/**
 * An object that matches, checks and fakes an {@link org.jmock.api.Invocation}
 * 
 * @author npryce
 * @author smgf
 */
public interface Expectation extends SelfDescribing {
    /**
     * Have enough {@link Invocation}s expected by this Expectation occurred?
     * 
     * @return
     *   <code>true</code> if the expectation has received enough
     *   of its expected invocations, <code>false</code> otherwise.
     */
    boolean isSatisfied();

    /**
     * Can more {@link Invocation}s expected by this Expectation still occur?
     * 
     * @return
     *   <code>true</code> if invocations expected by this expectation can still
     *   occur, <code>false</code> otherwise.
     */
    boolean allowsMoreInvocations();
    
	/**
     * Can the Expectation be invoked with <var>invocation</var>?
     * 
     * @param invocation
     * 
     * @return
     *   <code>true</code> if the expectation can be invoked with
     *   <var>invocation</var>, <code>false</code> otherwise.
	 */
    boolean matches(Invocation invocation);
    
    void describeMismatch(Invocation invocation, Description description);

    /**
     * Invokes the expectation: records that the invocation has
     * occurred and fakes some behaviour in response.
     * 
     * @param invocation
     *     The invocation to record and fake.
     * @return
     *     A result that is eventually returned from the method call
     *     that caused the invocation.
     *     
     * @throws Throwable
     *     An exception that is eventually thrown from the method call
     *     that caused the invocation.
     * @throws IllegalStateException
     *     The expectation has been invoked with a method that it doesn't
     *     match or the faked behaviour has been set up incorrectly.  
     *     For example, IllegalStateException is thrown when trying to return
     *     a value or throw a checked exception that is incompatible with the
     *     return type of the method being mocked
     */
	Object invoke(Invocation invocation) throws Throwable;

}
