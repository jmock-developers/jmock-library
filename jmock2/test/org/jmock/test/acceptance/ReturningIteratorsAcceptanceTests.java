package org.jmock.test.acceptance;

import java.util.Enumeration;
import java.util.Iterator;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class ReturningIteratorsAcceptanceTests extends TestCase {
    
    public interface Iterators {
        Iterator<String> i();
        Enumeration<String> e();
    }
    
    Mockery context = new Mockery();
    Iterators iterators = context.mock(Iterators.class);
    
    public void testReturnsIteratorsOverCollectionOfValues() {
        context.checking(new Expectations() {{
            allowing (iterators).i(); will(returnIterator("a", "b", "c"));
        }});
        
        assertIteratorOverSequence(iterators.i(), "a", "b", "c");

        // We get a new iterator every time
        assertIteratorOverSequence(iterators.i(), "a", "b", "c");
    }
    
    public void testReturnsEnumerationsOverCollectionOfValues() {
        context.checking(new Expectations() {{
            allowing (iterators).e(); will(returnEnumeration("x", "y", "z"));
        }});
        
        assertEnumerationOverSequence(iterators.e(), "x", "y", "z");

        // We get a new iterator every time
        assertEnumerationOverSequence(iterators.e(), "x", "y", "z");
    }
    
    private <T> void assertIteratorOverSequence(Iterator<?> e, T... values) {
        for (int i = 0; i < values.length; i++) {
            assertEquals("element " + i,
                         values[i], e.next());
        }
        
        assertTrue("at end", !e.hasNext());
    }
    
    private <T> void assertEnumerationOverSequence(Enumeration<?> e, T... values) {
        for (int i = 0; i < values.length; i++) {
            assertEquals("element " + i,
                         values[i], e.nextElement());
        }
        
        assertTrue("at end", !e.hasMoreElements());
    }

}
