package org.jmock.test.unit.lib.action;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.jmock.core.Action;
import org.jmock.core.Invocation;
import org.jmock.lib.action.ReturnIteratorAction;
import org.jmock.test.unit.support.GetDescription;

public class ReturnIteratorActionTest extends TestCase {
    private static final Object[] resultElements = {"0", "1", "2", "3"};
    
    public void testReturnsIteratorOverContentsOfCollection() throws Throwable {
        Collection collection = collectionOf(resultElements);
        Action action = new ReturnIteratorAction(collection);
        
        assertIteratorOverSequence((Iterator)action.invoke(ANY_INVOCATION), resultElements);
    }
    
    public void testReturnsNewIteratorOnEachInvocation() throws Throwable {
        Collection collection = collectionOf(resultElements);
        Action action = new ReturnIteratorAction(collection);
        
        assertIteratorOverSequence((Iterator)action.invoke(ANY_INVOCATION), resultElements);
        assertIteratorOverSequence((Iterator)action.invoke(ANY_INVOCATION), resultElements);
        assertIteratorOverSequence((Iterator)action.invoke(ANY_INVOCATION), resultElements);
    }
    
    public void testCanReturnIteratorOverArray() throws Throwable {
        Action action = new ReturnIteratorAction(resultElements);
        
        assertIteratorOverSequence((Iterator<?>)action.invoke(ANY_INVOCATION), resultElements);
    }
    
    public void testHasAReadableDescription() {
        Action action = new ReturnIteratorAction(resultElements);
        
        assertEquals("return iterator over \"0\", \"1\", \"2\", \"3\"", 
                	 GetDescription.of(action));
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
