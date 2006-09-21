package org.jmock.lib;

import org.jmock.core.MockObjectNamingScheme;

/**
 * A naming scheme in which the implicit name for a mock object is
 * the mocked type's name with the first character in lower case.
 * E.g. A mock object of type HelloWorld would be called "helloWorld".
 * 
 * This is the naming scheme used by default.
 * 
 * @author npryce
 *
 */
public class DefaultNamingScheme implements MockObjectNamingScheme {
    public static final DefaultNamingScheme INSTANCE = new DefaultNamingScheme();
    
    public String defaultNameFor(Class<?> typeToMock) {
        String simpleName = typeToMock.getSimpleName();
        
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
