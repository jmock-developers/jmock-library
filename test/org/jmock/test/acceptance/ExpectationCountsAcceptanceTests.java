package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;

@SuppressWarnings("unused")
public class ExpectationCountsAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    public void testOne() {
        context.checking(new Expectations() {{
            oneOf (mock).doSomething();
        }});
        
        assertContextIsNotSatisfied();
        mock.doSomething();
        context.assertIsSatisfied();
        assertAnotherInvocationFailsTheTest();
    }
    
    public void testExpectsExactly() {
        context.checking(new Expectations() {{
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
        context.checking(new Expectations() {{
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
        context.checking(new Expectations() {{
            atMost(2).of (mock).doSomething();
        }});
        
        context.assertIsSatisfied();
        mock.doSomething();
        context.assertIsSatisfied();
        mock.doSomething();
        assertAnotherInvocationFailsTheTest();
    }
    
    public void testExpectsBetween() {
        context.checking(new Expectations() {{
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
        context.checking(new Expectations() {{
            allowing (mock).doSomething();
        }});
        
        for (any_number of : times) {
            mock.doSomething();
            context.assertIsSatisfied();
        }
    }
    
    public void testNever() {
        context.checking(new Expectations() {{
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
