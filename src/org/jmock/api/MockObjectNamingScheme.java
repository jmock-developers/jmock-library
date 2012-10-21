package org.jmock.api;

/**
 * Creates names for mock objects that have not explicitly been
 * given a name.
 * 
 * @author npryce
 */
public interface MockObjectNamingScheme {
    /**
     * Derive a name for a mock object from the name of the given type.
     * 
     * @param typeToMock
     *     The type being mocked.
     * @return
     *     The default name for a mock object of the given type.
     */
    String defaultNameFor(Class<?> typeToMock);
}
