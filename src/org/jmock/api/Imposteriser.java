package org.jmock.api;

/**
 * An object that can creates a proxy of the given type to capture
 * {@link org.jmock.api.Invocation}s and pass them to an 
 * {@link org.jmock.api.Invokable} object for mocking or stubbing.
 * 
 * @author npryce
 */
public interface Imposteriser {
    /**
     * Reports if the Imposteriser is able to imposterise a given type.
     * 
     * @param type
     *     The type in question.
     * @return
     *     True if this imposteriser can imposterise <var>type</var>, false otherwise.
     */
    boolean canImposterise(Class<?> type);
    
    /**
     * Creates an imposter for a given type that forwards {@link Invocation}s to an 
     * {@link Invokable} object.
     * 
     * @param <T>
     *    The static type of the imposter that is created.
     * @param mockObject
     *    The object that is to receive invocations forwarded from the imposter.
     * @param mockedType
     *    The class representing the static type of the imposter.
     * @param ancilliaryTypes
     *    Other types for the imposter. It must be possible to dynamically cast the imposter to these types.
     *    These types must all be interfaces because Java only allows single inheritance of classes.
     * @return
     *    A new imposter.  The imposter must implement the mockedType and all the ancialliaryTypes.
     */
    <T> T imposterise(Invokable mockObject, Class<T> mockedType, Class<?>... ancilliaryTypes);
}
