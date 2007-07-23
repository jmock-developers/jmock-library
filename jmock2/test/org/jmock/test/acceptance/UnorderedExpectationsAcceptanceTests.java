package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;

public class UnorderedExpectationsAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    private void setUpUnorderedExpectations() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).method1();
            exactly(1).of (mock).method2();
            exactly(1).of (mock).method3();
        }});
    }

    public void testAllowsExpectedInvocationsInAnyOrder() {
        setUpUnorderedExpectations();
        mock.method1();
        mock.method2();
        mock.method3();
        context.assertIsSatisfied();
        
        setUpUnorderedExpectations();
        mock.method1();
        mock.method3();
        mock.method2();
        context.assertIsSatisfied();
        
        setUpUnorderedExpectations();
        mock.method2();
        mock.method1();
        mock.method3();
        context.assertIsSatisfied();
        
        setUpUnorderedExpectations();
        mock.method2();
        mock.method3();
        mock.method1();
        context.assertIsSatisfied();
        
        setUpUnorderedExpectations();
        mock.method3();
        mock.method1();
        mock.method2();
        context.assertIsSatisfied();
        
        setUpUnorderedExpectations();
        mock.method3();
        mock.method2();
        mock.method1();
        context.assertIsSatisfied();
    }
    
    public void testDoesNotAllowTooManyInvocationsOfExpectedMethods() {
        setUpUnorderedExpectations();
        mock.method1();
        try {
            mock.method1();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    public void testDoesNotAllowInvocationsOfUnexpectedMethods() {
        setUpUnorderedExpectations();
        try {
            mock.method4();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }

    public void testAllExpectationsMustBeSatisfied() {
        setUpUnorderedExpectations();
        try {
            context.assertIsSatisfied();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    public void testMatchesMethodsInFirstInFirstOutOrder() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).returnString(); will(returnValue("1"));
            exactly(1).of (mock).returnString(); will(returnValue("2"));
            exactly(1).of (mock).returnString(); will(returnValue("3"));
        }});
        
        assertEquals("1", mock.returnString());
        assertEquals("2", mock.returnString());
        assertEquals("3", mock.returnString());
    }
}
