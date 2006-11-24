package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.InThisOrder;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;

public class OrderedExpectationsAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    private void setUpOrderedExpectations() {
        context.expects(new InThisOrder() {{
            exactly(1).of (mock).method1();
            exactly(1).of (mock).method2();
            exactly(1).of (mock).method3();
        }});
    }
    
    public void testAllowsExpectedInvocationsInGivenOrder() {
        setUpOrderedExpectations();
        mock.method1();
        mock.method2();
        mock.method3();
        context.assertIsSatisfied();
    }
    
    public void testDoesNotAllowInvocationsInAnotherOrder() {
        setUpOrderedExpectations();
        try {
            mock.method1();
            mock.method3();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    public void testDoesNotAllowTooManyInvocationsOfExpectedMethods() {
        setUpOrderedExpectations();
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
        setUpOrderedExpectations();
        try {
            mock.method4();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }

    public void testAllExpectationsMustBeSatisfied() {
        setUpOrderedExpectations();
        try {
            context.assertIsSatisfied();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
}
