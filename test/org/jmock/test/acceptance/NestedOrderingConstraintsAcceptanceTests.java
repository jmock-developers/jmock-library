package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.InAnyOrder;
import org.jmock.InThisOrder;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;

public class NestedOrderingConstraintsAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    
    MockedType mock = context.mock(MockedType.class, "mock");
    
    private void setUpExpectations() {
        context.expects(new InAnyOrder() {{
            exactly(1).of (mock).doSomething();
            expects(new InThisOrder() {{
                exactly(1).of (mock).method1();
                exactly(1).of (mock).method2();
            }});
        }});
    }
    
    public void testNestedUnorderedAndOrderedExpectationsAllowIntertwingledInvocations() {
        setUpExpectations();
        mock.doSomething();
        mock.method1();
        mock.method2();
        context.assertIsSatisfied();
        
        setUpExpectations();
        mock.method1();
        mock.doSomething();
        mock.method2();
        context.assertIsSatisfied();
        
        setUpExpectations();
        mock.method1();
        mock.method2();
        mock.doSomething();
        context.assertIsSatisfied();
    }
    
    public void testNestedOrderingConstraintsDetectOrderingFailureWhenUnorderedExpectationHasBeenMet() {
        setUpExpectations();
        mock.doSomething();
        try {
            mock.method2();
            fail("should have violated ordering constraint");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    public void testNestedOrderingConstraintsDetectOrderingFailureWhenUnorderedExpectationHasNotBeenMet() {
        setUpExpectations();
        try {
            mock.method2();
            fail("should have violated ordering constraint");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
}
