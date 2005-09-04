package org.jmock.core.constraint;

import java.util.Arrays;
import java.util.Collection;

import org.jmock.core.Constraint;
import org.jmock.core.Formatting;

public class IsIn implements Constraint {
    private final Collection collection;

    public IsIn(Collection collection) {
        this.collection = collection;
    }
    
    public IsIn(Object[] elements) {
        collection = Arrays.asList(elements);
    }

    public boolean eval(Object o) {
        return collection.contains(o);
    }

    public StringBuffer describeTo(StringBuffer buffer) {
        buffer.append("one of ");
        Formatting.join(collection, buffer, "{", "}");
        return buffer;
    }
}
