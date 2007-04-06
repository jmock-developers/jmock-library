package org.jmock.core.constraint;

import org.jmock.core.Constraint;

public class IsCompatibleType implements Constraint {
    private final Class type;
    
    public IsCompatibleType(Class type) {
        this.type = type;
    }
    
    public boolean eval(Object o) {
        return o instanceof Class && type.isAssignableFrom((Class)o);
    }

    public StringBuffer describeTo(StringBuffer buffer) {
        return buffer.append("type < " + type.getName());
    }
}
