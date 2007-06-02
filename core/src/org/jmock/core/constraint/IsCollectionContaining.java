package org.jmock.core.constraint;

import java.util.Collection;
import java.util.Iterator;

import org.jmock.core.Constraint;

public class IsCollectionContaining implements Constraint {
    private final Constraint elementConstraint;

    public IsCollectionContaining(Constraint elementConstraint) {
        this.elementConstraint = elementConstraint;
    }
    
    public boolean eval(Object o) {
        return o != null
        	&& o instanceof Collection
        	&& collectionContainsMatchingElement((Collection)o);
    }
    
    private boolean collectionContainsMatchingElement(Collection collection) {
        Iterator i = collection.iterator();
        while (i.hasNext()) {
            if (elementConstraint.eval(i.next())) return true;
        }
        return false;
    }
    
    public StringBuffer describeTo(StringBuffer buffer) {
        buffer.append("a collection containing ");
        elementConstraint.describeTo(buffer);
        return buffer;
    }
}
