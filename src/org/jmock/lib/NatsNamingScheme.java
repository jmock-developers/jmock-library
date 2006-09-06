package org.jmock.lib;

import org.jmock.core.MockObjectNamingScheme;

public class NatsNamingScheme implements MockObjectNamingScheme {
    public static final NatsNamingScheme INSTANCE = new NatsNamingScheme();
    
    public String defaultNameFor(Class<?> typeToMock) {
        String simpleName = typeToMock.getSimpleName();
        
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }
}
