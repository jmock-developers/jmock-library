package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.hamcrest.StringDescription;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;
import org.jmock.test.unit.support.AssertThat;

public class ErrorMessagesAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    
    MockedType mock = context.mock(MockedType.class, "mock");
    
    public void testShowsExpectedAndCurrentNumberOfCallsInErrorMessage() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).method1();
            exactly(1).of (mock).method2();
            atLeast(1).of (mock).method3();
            allowing (mock).method4();
        }});
        
        mock.method2();
        mock.method3();
        mock.method4();
        
        try {
            context.assertIsSatisfied();
        }
        catch (ExpectationError e) {
            String message = StringDescription.toString(e);
            
            AssertThat.stringIncludes("should include expectation that has not been invoked at all",
                                      "method1", message);
            AssertThat.stringIncludes("should include expectation that has been fully satisfied",
                                      "method2", message);
            AssertThat.stringIncludes("should include expectation that has been satisfied but can still be invoked",
                                      "method3", message);
            AssertThat.stringIncludes("should include expectation that is allowed",
                                      "method4", message);     
        }
    }

    // See issue JMOCK-132
    public void testErrorMessageIncludesNotInvokedInsteadOfInvokedExactly0Times() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).method1();
        }});
        
        try {
            context.assertIsSatisfied();
        }
        catch (ExpectationError e) {
            String message = StringDescription.toString(e);
            
            AssertThat.stringIncludes("should include 'never invoked'", 
                                      "never invoked", message);
        }
    }
}
