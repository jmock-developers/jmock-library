package org.jmock.test.acceptance;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class DoAllAcceptanceTests extends TestCase {
    public interface Collector {
        void addThingsTo(Collection<String> collection);
    }
    
    Mockery context = new Mockery();
    Collector collector = context.mock(Collector.class);
    
    
    public void testCanSpecifyMultipleStubsForOneInvocation() {
        final ArrayList<String> list = new ArrayList<String>();
        
        context.checking(new Expectations() {{
            exactly(1).of (collector).addThingsTo(with(same(list)));
            	will(doAll(addElement("1"), 
                           addElement("2"), 
                           addElement("3"), 
                           addElement("4")));
        }});
        
        collector.addThingsTo(list);
        
        assertEquals("list length", 4, list.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals("element "+(i+1), Integer.toString(i+1), list.get(i));
        }
    }
    
    private Action addElement(String newElement) {
        return new AddElementAction(newElement);
    }
    
    public static class AddElementAction implements Action {
        private final String newElement;

        public AddElementAction(String newElement) {
            this.newElement = newElement;
        }

        @SuppressWarnings("unchecked")
        public Object invoke(Invocation invocation) throws Throwable {
            ((Collection<Object>)invocation.getParameter(0)).add(newElement);
            return null;
        }
        
        public void describeTo(Description description) {
            throw new UnsupportedOperationException("AddElementStub.describeTo not implemented");
        }
    }
}
