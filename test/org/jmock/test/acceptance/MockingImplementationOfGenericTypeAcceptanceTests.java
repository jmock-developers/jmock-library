package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;


public class MockingImplementationOfGenericTypeAcceptanceTests extends TestCase {
    private Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    public void testWhenDefinedAndInvokedThroughClass() throws Exception {
        final AnImplementation mock = context.mock(AnImplementation.class);

        context.checking(new Expectations() {{
            oneOf (mock).doSomethingWith("a");
        }});
            
        mock.doSomethingWith("a");
    }
    
    public void testWhenDefinedThroughClassAndInvokedThroughMethod() throws Exception {
        final AnImplementation mock = context.mock(AnImplementation.class);

        context.checking(new Expectations() {{
            oneOf (mock).doSomethingWith("a");
        }});
        
        // Note: this is invoked through a "bridge" method and so the method
        // invoked when expectations are checked appears to be different from
        // that invoked when expectations are captured.
        ((AnInterface<String>)mock).doSomethingWith("a");
    }
    
    public void testWhenDefinedAndInvokedThroughInterface() throws Exception {
        final AnInterface<String> mock = context.mock(AnImplementation.class);

        context.checking(new Expectations() {{
            oneOf (mock).doSomethingWith("a");
        }});

        mock.doSomethingWith("a");
    }

    public interface AnInterface<T> {
        void doSomethingWith(T arg);
    }

    public static class AnImplementation implements AnInterface<String> {
        public void doSomethingWith(String arg) {
        }
    }
}
