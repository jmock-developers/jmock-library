package org.jmock.core;

public interface Imposteriser {
    <T> T imposterise(Invokable mockObject, Class<T> mockedType, Class<?>... ancilliaryTypes);
}
