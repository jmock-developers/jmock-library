package org.jmock.core.constraint;

import java.lang.reflect.Array;

import org.jmock.core.Constraint;

public class IsArrayContaining implements Constraint {
    private final Constraint elementConstraint;

    public IsArrayContaining(Constraint elementConstraint) {
        this.elementConstraint = elementConstraint;
    }
    
    public boolean eval(Object o) {
        return o != null
        	&& o.getClass().isArray()
        	&& arrayContainsMatchingElement(o);
    }
    
    private boolean arrayContainsMatchingElement(Object array) {
        int size = Array.getLength(array);
        for (int i = 0; i < size; i++) {
            if (elementConstraint.eval(Array.get(array, i))) return true;
        }
        return false;
    }
    
    public StringBuffer describeTo(StringBuffer buffer) {
        buffer.append("an array containing ");
        elementConstraint.describeTo(buffer);
        return buffer;
    }
}
