package org.jmock.lib;

import org.jmock.core.MockObjectNamingScheme;

/**
 * A naming scheme in which the default name for a mock object is
 * the mocked type's name prepend with "mock".
 * 
 * E.g. A mock object of type HelloWorld would be called "mockHelloWorld".
 * 
 * @author npryce
 *
 */
public class StevesNamingScheme implements MockObjectNamingScheme {
    public static final StevesNamingScheme INSTANCE = new StevesNamingScheme();
    
    public String defaultNameFor(Class<?> typeToMock) {
        return "mock" + typeToMock.getSimpleName();
    }
}
