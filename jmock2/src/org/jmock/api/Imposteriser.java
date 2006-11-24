package org.jmock.api;

/**
 * An object that can creates a proxy of the given type to capture
 * {@link org.jmock.api.Invocation}s and pass them to an 
 * {@link org.jmock.api.Invokable} object for mocking or stubbing.
 * 
 * @author npryce
 */
public interface Imposteriser {
    <T> T imposterise(Invokable mockObject, Class<T> mockedType, Class<?>... ancilliaryTypes);
}
