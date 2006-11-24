package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.InAnyOrder;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;
import org.jmock.api.ExpectationErrorTranslator;

public class ExpectationErrorTranslationAcceptanceTests extends TestCase {
    public class TranslatedError extends Error {}
    
    ExpectationErrorTranslator translator = new ExpectationErrorTranslator() {
        public Error translate(ExpectationError e) {
            return new TranslatedError();
        }
    };
    
    Mockery context = new Mockery();
    
    MockedType mock = context.mock(MockedType.class, "mock");
    
    public void setUp() {
        context.setExpectationErrorTranslator(translator);
    }
    
    public void testMockeryCanTranslateExpectationErrorsIntoDifferentExceptionTypeWhenUnexpectedInvocationOccurs() {
        context.expects(new InAnyOrder() {{
            exactly(1).of (mock).method1();
        }});
        
        try {
            mock.method2();
        }
        catch (TranslatedError e) {
            // expected
        }
        catch (ExpectationError e) {
            fail("should have translated ExpectationError into TranslatedError");
        }
    }
    
    public void testMockeryCanTranslateExpectationErrorsIntoDifferentExceptionTypeWhenMockeryIsNotSatisfied() {
        context.expects(new InAnyOrder() {{
            exactly(1).of (mock).method1();
        }});
        
        try {
            context.assertIsSatisfied();
        }
        catch (TranslatedError e) {
            // expected
        }
        catch (ExpectationError e) {
            fail("should have translated ExpectationError into TranslatedError");
        }
    }
}
