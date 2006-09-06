package org.jmock.test.acceptance;

import org.jmock.InAnyOrder;
import org.jmock.Mockery;

import junit.framework.TestCase;

public class ParameterMatchingAcceptanceTests extends TestCase {
    public interface AnInterface {
        void doSomethingWith(String s);
    }
    
    Mockery context = new Mockery();
    AnInterface mock = context.mock(AnInterface.class, "mock");
    
    public void testMatchesParameterValues() {
        context.expects(new InAnyOrder() {{
            exactly(1).of (mock).doSomethingWith(with(equal("hello")));
            exactly(1).of (mock).doSomethingWith(with(equal("goodbye")));
        }});
        
        mock.doSomethingWith("hello");
        mock.doSomethingWith("goodbye");
        
        context.assertIsSatisfied();
    }
    
    public void testDoesNotAllowUnexpectedParameterValues() {
        context.expects(new InAnyOrder() {{
            exactly(1).of (mock).doSomethingWith(with(equal("hello")));
            exactly(1).of (mock).doSomethingWith(with(equal("goodbye")));
        }});
        
        mock.doSomethingWith("hello");
        mock.doSomethingWith("goodbye");
        
        context.assertIsSatisfied();
    }
}
