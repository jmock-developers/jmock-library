package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.api.ExpectationError;

public class OverrideExpectationsFromSetUpAcceptanceTests extends TestCase {
    Mockery mockery = new Mockery();
    MockedType mock = mockery.mock(MockedType.class, "mock");
    
    States test = mockery.states("test").startsAs("settingUp");
    
    @Override
    public void setUp() {
        mockery.checking(new Expectations() {{
            allowing (mock).doSomethingWith(with(any(String.class))); when(test.is("settingUp"));
        }});
        
        // These would be called by the object under test, during set-up
        mock.doSomethingWith("foo");
        mock.doSomethingWith("bar");
        
        test.become("ready");
    }
    
    public void testSomething() {
        mockery.checking(new Expectations() {{
            oneOf (mock).doSomethingWith("whee");
        }});
        
        try {
            // This would be called by the object under test, during the test.
            // It should be detected as a test failure, because the 'allowing'
            // expectations defined during set-up no longer apply
            mock.doSomethingWith("whoo");
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError expected) {}
    }
}
