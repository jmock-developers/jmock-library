package org.jmock.core.stub;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.jmock.core.Formatting;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;


public class ReturnIteratorStub implements Stub 
{
    private Collection collection;
    
    public ReturnIteratorStub(Collection collection) {
        this.collection = collection;
    }
    
    public ReturnIteratorStub(Object[] array) {
        this.collection = Arrays.asList(array);
    }
    
    public Object invoke(Invocation invocation) throws Throwable {
        return collection.iterator();
    }
    
    public StringBuffer describeTo(StringBuffer buffer) {
        buffer.append("return iterator over ");
        boolean separate = false;
        for (Iterator i = collection.iterator(); i.hasNext();) {
            if (separate) buffer.append(", ");
            buffer.append(Formatting.toReadableString(i.next()));
            separate = true;
        }
        return buffer;
    }
}
