package org.jmock.lib.action;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

/**
 * Returns an {@link Iterator} over a collection.
 * 
 * @author nat
 */
public class ReturnIteratorAction implements Action {
    private Collection<?> collection;
    
    public ReturnIteratorAction(Collection<?> collection) {
        this.collection = collection;
    }
    
    public ReturnIteratorAction(Object... array) {
        this.collection = Arrays.asList(array);
    }
    
    public Iterator<?> invoke(Invocation invocation) throws Throwable {
        return collection.iterator();
    }
    
    public void describeTo(Description description) {
        description.appendValueList("return iterator over ", ", ", "", collection);
    }
}
