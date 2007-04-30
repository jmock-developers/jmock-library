package org.jmock.test.acceptance;

import java.util.Vector;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;
import org.jmock.lib.legacy.ClassImposteriser;

// Fixes issue JMOCK-96
public class RedeclaredObjectMethodsAcceptanceTests extends TestCase {
    public interface MockedInterface {
        String toString();
    }
    
    public static class MockedClass {
        @Override
        public String toString() {
            return "not mocked";
        }
    }
    
    public void testCanRedeclareObjectMethodsInMockedInterfaces() {
        Mockery context = new Mockery();
        MockedInterface mock = context.mock(MockedInterface.class, "X");
        
        assertEquals("X", mock.toString());
    }
    
    public void testCanRedeclareObjectMethodsInMockedClasses() {
        Mockery context = new Mockery();
        context.setImposteriser(ClassImposteriser.INSTANCE);
        MockedClass mock = context.mock(MockedClass.class, "X");
        
        assertEquals("X", mock.toString());
    }
    
    /* 
     * Adapted from Jira issue JMOCK-96
     */
    @SuppressWarnings({"cast", "unchecked"})
    public void testUseMockObjectHangs1() {
        Mockery context = new Mockery();
        context.setImposteriser(ClassImposteriser.INSTANCE);
        final Vector<Object> mock = (Vector<Object>)context.mock(Vector.class);
        
        context.checking(new Expectations() {{
            atLeast(1).of (mock).size(); will(returnValue(2));
        }});
        
        try {
            for (int i = 0; i < mock.size(); i++) {
                System.out.println("Vector entry " + i + " = " + mock.get(i));
            }
        }
        catch (ExpectationError error) {
            // expected
        }
    }
}
