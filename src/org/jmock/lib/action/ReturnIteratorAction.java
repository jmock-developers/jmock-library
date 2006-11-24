package org.jmock.lib.action;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;


public class ReturnIteratorAction implements Action {
    private Collection<?> collection;
    
    public ReturnIteratorAction(Collection<?> collection) {
        this.collection = collection;
    }
    
    public ReturnIteratorAction(Object... array) {
        this.collection = Arrays.asList(array);
    }
    
    public Object invoke(Invocation invocation) throws Throwable {
        return collection.iterator();
    }
    
    public void describeTo(Description description) {
        description.appendText("return iterator over ");
        boolean separate = false;
        for (Iterator i = collection.iterator(); i.hasNext();) {
            if (separate) description.appendText(", ");
            description.appendValue(i.next());
            separate = true;
        }
    }
}
