package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class ParameterMatchingAcceptanceTests extends TestCase {
    public interface AnInterface {
        void doSomethingWith(String s);
    }
    
    Mockery context = new Mockery();
    AnInterface mock = context.mock(AnInterface.class, "mock");
    
    public void testMatchesParameterValues() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).doSomethingWith(with(equal("hello")));
            exactly(1).of (mock).doSomethingWith(with(equal("goodbye")));
        }});
        
        mock.doSomethingWith("hello");
        mock.doSomethingWith("goodbye");
        
        context.assertIsSatisfied();
    }
    
    public void testDoesNotAllowUnexpectedParameterValues() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).doSomethingWith(with(equal("hello")));
            exactly(1).of (mock).doSomethingWith(with(equal("goodbye")));
        }});
        
        mock.doSomethingWith("hello");
        mock.doSomethingWith("goodbye");
        
        context.assertIsSatisfied();
    }
}
