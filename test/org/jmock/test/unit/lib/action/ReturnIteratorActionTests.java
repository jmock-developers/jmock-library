package org.jmock.test.unit.lib.action;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.hamcrest.StringDescription;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.lib.action.ReturnIteratorAction;

public class ReturnIteratorActionTests extends TestCase {
    private static final Object[] resultElements = {"0", "1", "2", "3"};
    
    public void testReturnsIteratorOverContentsOfCollection() throws Throwable {
        Collection<Object> collection = collectionOf(resultElements);
        ReturnIteratorAction action = new ReturnIteratorAction(collection);
        
        assertIteratorOverSequence(action.invoke(ANY_INVOCATION), resultElements);
    }
    
    public void testReturnsNewIteratorOnEachInvocation() throws Throwable {
        Collection<?> collection = collectionOf(resultElements);
        ReturnIteratorAction action = new ReturnIteratorAction(collection);
        
        assertIteratorOverSequence(action.invoke(ANY_INVOCATION), resultElements);
        assertIteratorOverSequence(action.invoke(ANY_INVOCATION), resultElements);
        assertIteratorOverSequence(action.invoke(ANY_INVOCATION), resultElements);
    }
    
    public void testCanReturnIteratorOverArray() throws Throwable {
        Action action = new ReturnIteratorAction(resultElements);
        
        assertIteratorOverSequence((Iterator<?>)action.invoke(ANY_INVOCATION), resultElements);
    }
    
    public void testHasAReadableDescription() {
        Action action = new ReturnIteratorAction(resultElements);
        
        assertEquals("return iterator over \"0\", \"1\", \"2\", \"3\"", 
                	 StringDescription.toString(action));
    }
    
    private <T> Collection<T> collectionOf(T... values) {
        return Arrays.asList(values);
    }
    
    private <T> void assertIteratorOverSequence(Iterator<?> iterator, T[] values) {
        for (int i = 0; i < values.length; i++) {
            assertEquals("element " + i,
                    	 values[i], iterator.next());
        }
    }
    
    private static final Invocation ANY_INVOCATION = null;
}
