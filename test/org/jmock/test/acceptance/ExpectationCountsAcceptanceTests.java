package org.jmock.test.acceptance;

import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;

import junit.framework.TestCase;

@SuppressWarnings("unused")
public class ExpectationCountsAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    public void testOne() {
        context.expects(new InAnyOrder() {{
            one (mock).doSomething();
        }});
        
        assertContextIsNotSatisfied();
        mock.doSomething();
        context.assertIsSatisfied();
        assertAnotherInvocationFailsTheTest();
    }
    
    public void testExpectsExactly() {
        context.expects(new InAnyOrder() {{
            exactly(2).of (mock).doSomething();
        }});
        
        assertContextIsNotSatisfied();
        mock.doSomething();
        assertContextIsNotSatisfied();
        mock.doSomething();
        context.assertIsSatisfied();
        assertAnotherInvocationFailsTheTest();
    }
    
    public void testExpectsAtLeast() {
        context.expects(new InAnyOrder() {{
            atLeast(2).of (mock).doSomething();
        }});
        
        assertContextIsNotSatisfied();
        mock.doSomething();
        assertContextIsNotSatisfied();
        mock.doSomething();
        context.assertIsSatisfied();
        
        for (any_number of : times) {
            mock.doSomething();
            context.assertIsSatisfied();
        }
    }
    
    public void testExpectsAtMost() {
        context.expects(new InAnyOrder() {{
            atMost(2).of (mock).doSomething();
        }});
        
        context.assertIsSatisfied();
        mock.doSomething();
        context.assertIsSatisfied();
        mock.doSomething();
        assertAnotherInvocationFailsTheTest();
        context.assertIsSatisfied();
    }
    
    public void testExpectsBetween() {
        context.expects(new InAnyOrder() {{
            between(2,3).of (mock).doSomething();
        }});
        
        assertContextIsNotSatisfied();
        mock.doSomething();
        assertContextIsNotSatisfied();
        mock.doSomething();
        context.assertIsSatisfied();
        mock.doSomething();
        context.assertIsSatisfied();
        
        assertAnotherInvocationFailsTheTest();
    }
    
    public void testAllows() {
        context.expects(new InAnyOrder() {{
            allowing (mock).doSomething();
        }});
        
        for (any_number of : times) {
            mock.doSomething();
            context.assertIsSatisfied();
        }
    }
    
    public void testNever() {
        context.expects(new InAnyOrder() {{
            never (mock).doSomething();
        }});
        
        context.assertIsSatisfied();
        assertAnotherInvocationFailsTheTest();
    }
    
    private void assertAnotherInvocationFailsTheTest() {
        try {
            mock.doSomething();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    private void assertContextIsNotSatisfied() {
        try {
            context.assertIsSatisfied();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    private class any_number {}
    static final any_number[] times = new any_number[4];
}
