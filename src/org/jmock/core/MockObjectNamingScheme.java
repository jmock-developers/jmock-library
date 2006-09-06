package org.jmock.core;

public interface MockObjectNamingScheme {
    String defaultNameFor(Class<?> typeToMock);
}
