package org.jmock.core.constraint;

import org.jmock.core.Constraint;

public class HasToString implements Constraint {
    private final Constraint toStringConstraint;
    
    public HasToString(Constraint toStringConstraint) {
        this.toStringConstraint = toStringConstraint;
    }
    
    public boolean eval(Object o) {
        return toStringConstraint.eval(o.toString());
    }
    
    public StringBuffer describeTo(StringBuffer buffer) {
        buffer.append("toString(");
        toStringConstraint.describeTo(buffer);
        buffer.append(")");
        return buffer;
    }
}
