package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Mockery;
import org.jmock.lib.nonstd.UnsafeHackConcreteClassImposteriser;

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
        context.setImposteriser(new UnsafeHackConcreteClassImposteriser());
        MockedClass mock = context.mock(MockedClass.class, "X");
        
        assertEquals("X", mock.toString());
    }
}
