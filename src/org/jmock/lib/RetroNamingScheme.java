package org.jmock.lib;

import org.jmock.api.MockObjectNamingScheme;

/**
 * A naming scheme in which the implicit name for a mock object is
 * the mocked type's name prepend with "mock".
 * 
 * E.g. A mock object of type HelloWorld would be called "mockHelloWorld".
 * 
 * This was the naming scheme used at Connextra and in many early examples
 * of TDD with mock objects.
 * 
 * @author npryce
 *
 */
public class RetroNamingScheme implements MockObjectNamingScheme {
    public static final RetroNamingScheme INSTANCE = new RetroNamingScheme();
    
    public String defaultNameFor(Class<?> typeToMock) {
        return "mock" + typeToMock.getSimpleName();
    }
}
