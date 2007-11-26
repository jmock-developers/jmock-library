package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class ParameterMatchingAcceptanceTests extends TestCase {
    public interface AnInterface {
        void doSomethingWith(String s);
        void doSomethingWithBoth(String s1, String s2);
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
    
    public void testAllOrNoneOfTheParametersMustBeSpecifiedByMatchers() {
        try {
            context.checking(new Expectations() {{
                exactly(1).of (mock).doSomethingWithBoth(with(equal("a-matcher")), "not-a-matcher");
            }});
        }
        catch (IllegalArgumentException expected) {
        }
    }
    
    // Test to show that issue JMOCK-160 is spurious
    public void testNotAllExpectationsOfSameMockMustUseMatchers() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).doSomethingWithBoth(with(equal("x")), with(equal("y")));
            exactly(1).of (mock).doSomethingWith("z");
        }});
        
        mock.doSomethingWithBoth("x", "y");
        mock.doSomethingWith("z");
        
        context.assertIsSatisfied();
    }
}
