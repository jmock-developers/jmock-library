package org.jmock.core.constraint;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jmock.core.Constraint;

public class IsMapContaining implements Constraint {
    private final Constraint keyConstraint;
    private final Constraint valueConstraint;

    public IsMapContaining(Constraint keyConstraint, Constraint valueConstraint) {
        this.keyConstraint = keyConstraint;
        this.valueConstraint = valueConstraint;
    }
    
    public boolean eval(Object o) {
        if (o != null && o instanceof Map) {
	        Map map = (Map)o;
	        
	        for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
	            Map.Entry entry = (Entry) i.next();
	            
	            if (keyConstraint.eval(entry.getKey()) && valueConstraint.eval(entry.getValue())) {
	                return true;
	            }
	        }
        }
	    
        return false;
    }

    public StringBuffer describeTo(StringBuffer buffer) {
        buffer.append("map containing [");
        keyConstraint.describeTo(buffer);
        buffer.append("->");
        valueConstraint.describeTo(buffer);
        buffer.append("]");
        return buffer;
    }
}
