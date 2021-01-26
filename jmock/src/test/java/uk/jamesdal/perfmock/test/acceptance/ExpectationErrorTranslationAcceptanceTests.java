package uk.jamesdal.perfmock.test.acceptance;

import junit.framework.TestCase;

import uk.jamesdal.perfmock.Expectations;
import uk.jamesdal.perfmock.Mockery;
import uk.jamesdal.perfmock.api.ExpectationError;
import uk.jamesdal.perfmock.api.ExpectationErrorTranslator;

public class ExpectationErrorTranslationAcceptanceTests extends TestCase {
    public class TranslatedError extends Error {}
    
    ExpectationErrorTranslator translator = new ExpectationErrorTranslator() {
        public Error translate(ExpectationError e) {
            return new TranslatedError();
        }
    };
    
    Mockery context = new Mockery();
    
    MockedType mock = context.mock(MockedType.class, "mock");
    
    @Override
    public void setUp() {
        context.setExpectationErrorTranslator(translator);
    }
    
    public void testMockeryCanTranslateExpectationErrorsIntoDifferentExceptionTypeWhenUnexpectedInvocationOccurs() {
        context.checking(new Expectations() {{
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
        context.checking(new Expectations() {{
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
