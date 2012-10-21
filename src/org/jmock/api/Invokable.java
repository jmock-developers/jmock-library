package org.jmock.api;

/**
 * An object that can receive an {@link org.jmock.api.Invocation}.
 * 
 * @author npryce
 */
public interface Invokable {
    /**
     * Performs an action in response to an invocation.
     *
     * @param invocation 
     *     The invocation to perform.
     * @return 
     *     The result of the invocation, if not throwing an exception.
     *     Must return <code>null</code> if the invoked method has a void return type.
     * @throws Throwable 
     *     An exception to be thrown to the caller, if not returning a value.  Any checked exception
     *     thrown must be in the <code>throws</code> list of the invoked method.
     */
    Object invoke(Invocation invocation) throws Throwable;
}
