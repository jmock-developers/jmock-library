package org.jmock.lib;

import org.jmock.core.MockObjectNamingScheme;

public class StevesNamingScheme implements MockObjectNamingScheme {
    public static final StevesNamingScheme INSTANCE = new StevesNamingScheme();
    
    public String defaultNameFor(Class<?> typeToMock) {
        return "mock" + typeToMock.getSimpleName();
    }
}
