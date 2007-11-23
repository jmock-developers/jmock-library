package org.jmock.lib.action;

import static java.util.Collections.enumeration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

/**
 * Returns an {@link Enumeration} over a collection.
 * 
 * @author nat
 */
public class ReturnEnumerationAction implements Action {
    private Collection<?> collection;
    
    public ReturnEnumerationAction(Collection<?> collection) {
        this.collection = collection;
    }
    
    public ReturnEnumerationAction(Object... array) {
        this.collection = Arrays.asList(array);
    }
    
    public Enumeration<?> invoke(Invocation invocation) throws Throwable {
        return enumeration(collection);
    }
    
    public void describeTo(Description description) {
        description.appendValueList("return enumeration over ", ", ", "", collection);
    }
}
