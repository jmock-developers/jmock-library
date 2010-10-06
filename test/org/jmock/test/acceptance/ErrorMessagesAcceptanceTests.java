package org.jmock.test.acceptance;

import junit.framework.TestCase;
import org.hamcrest.StringDescription;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.ExpectationError;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.jmock.test.unit.support.AssertThat;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.StringDescription.asString;
import static org.junit.Assert.assertThat;

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
    
    // See issue JMOCK-153
    public void testErrorMessageIncludesOnceInsteadOfExactly1Time() {
        context.checking(new Expectations() {{
            exactly(1).of (mock).method1();
        }});
        
        try {
            context.assertIsSatisfied();
        }
        catch (ExpectationError e) {
            String message = StringDescription.toString(e);
            
            AssertThat.stringIncludes("should include 'once'", 
                                      "once", message);
        }
    }
    
    // See issue JMOCK-190
    public void testCannotExpectToString() {
        try {
            context.checking(new Expectations() {{
                allowing(mock).toString();
            }});
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException expected) {} 
    }
    
    // See issue JMOCK-190
    public void testCannotExpectEquals() {
        try {
            context.checking(new Expectations() {{
                allowing(mock).equals("any object");
            }});
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException expected) {}
    }
    
    // See issue JMOCK-190
    public void testCannotExpectHashCode() {
        try {
            context.checking(new Expectations() {{
                allowing(mock).hashCode();
            }});
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException expected) {}
    }
    
    public interface TypeThatMakesFinalizePublic {
        public void finalize();
    }
    
    // See issue JMOCK-190
    public void testCannotExpectFinalize() {
        final TypeThatMakesFinalizePublic mockWithFinalize = context.mock(TypeThatMakesFinalizePublic.class, "mockWithFinalize");
        
        try {
            context.checking(new Expectations() {{
                allowing(mockWithFinalize).finalize();
            }});
            fail("should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException expected) {}
    }
    
    // See issue JMOCK-167
    public void testDoesNotDescribeReturnValueForMethodsThatAreKnownToBeVoid() {
        context.checking(new Expectations() {{
            oneOf (mock).doSomething();
        }});
        
        assertThat(asString(context),
                   not(containsString("returns a default value")));
    }

    public void testMismatchDescription() {
        context.checking(new Expectations() {{
            oneOf (mock).doSomethingWith("foo");
            oneOf (mock).doSomethingWith("x", "y"); will(doSomethingDescribedAs("ACTION"));
        }});

        try {
            mock.doSomethingWith("X", "Y");
        } catch (ExpectationError e) {
            String failure = asString(e);
            
            Integer actionIndex = failure.indexOf("ACTION");
            Integer parameterMismatchIndex = failure.indexOf("parameter 0 did not match");

            assertTrue("action should come before parameter mismatches in description",
                      actionIndex < parameterMismatchIndex);
        }
    }

    public Action doSomethingDescribedAs(String name) {
        return new CustomAction(name) {
            @Override
            public Object invoke(Invocation invocation) throws Throwable {
                return null;
            }
        };
    }
}
