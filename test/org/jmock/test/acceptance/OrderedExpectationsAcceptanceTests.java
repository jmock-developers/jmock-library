package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;

public class OrderedExpectationsAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    private void setUpOrderedExpectations() {
        context.checking(new Expectations() {{
            allowing (mock).method1();
                named("first");
            allowing (mock).method2();
                after("first");
                
            allowing (mock).method3();
                before("last");
            allowing (mock).method4();
                named("last");
        }});
    }
    
    public void testAllowsExpectedInvocationsInGivenOrder() {
        setUpOrderedExpectations();
        mock.method1();
        mock.method2();
        
        mock.method3();
        mock.method4();
        context.assertIsSatisfied();
    }
    
    public void testDoesNotAllowInvocationOfAfterExpectationInWrongOrder() {
        setUpOrderedExpectations();
        try {
            mock.method2();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
    
    public void testDoesNotAllowInvocationOfBeforeExpectationInWrongOrder() {
        setUpOrderedExpectations();
        try {
            mock.method4();
            mock.method3();
            fail("should have thrown ExpectationError");
        }
        catch (ExpectationError e) {
            // expected
        }
    }
}
