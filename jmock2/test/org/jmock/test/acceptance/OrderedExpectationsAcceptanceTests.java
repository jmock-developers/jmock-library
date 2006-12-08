package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.InThisOrder;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;
import org.jmock.test.acceptance.ThrowingExceptionsAcceptanceTests.UncheckedException;

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
    
    public void testReportsUnsatisfiedExpectationsAfterAnExceptionIsThrown() {
        Mockery mockery = new Mockery();
        final MockedType thrower = mockery.mock(MockedType.class);

        mockery.expects(new InThisOrder() {{
            one (thrower).method1(); will(throwException(new UncheckedException()));
            one (thrower).method2(); 
        }});

        try {
            thrower.method1();
        } catch (UncheckedException expected) {};
        
        try {
            mockery.assertIsSatisfied();
            fail("should have thrown ExpectationError");
        } catch (ExpectationError e) {
            // expected
        }
    }
}
