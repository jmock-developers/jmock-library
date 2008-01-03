package org.jmock.test.acceptance;

import static org.hamcrest.StringDescription.asString;
import static org.jmock.test.unit.support.StringContainsInOrder.containsInOrder;
import static org.junit.Assert.assertThat;
import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.ExpectationError;

public class RecordingAcceptanceTests extends TestCase {
    Mockery context = new Mockery();
    MockedType mock = context.mock(MockedType.class, "mock");
    
    public void testRecordsActualInvocations() {
        context.checking(new Expectations() {{
            allowing (same(mock));
        }});
        
        mock.doSomething();
        mock.doSomethingWith("foo");
        mock.doSomethingWith("x", "y");
        
        assertThat(asString(context), containsInOrder(
            "what happened:",
            "mock.doSomething()",
            "mock.doSomethingWith(\"foo\")",
            "mock.doSomethingWith(\"x\", \"y\")"));
    }

    static class ExampleException extends RuntimeException {}
    
    public void testRecordsInvocationsThatThrowExceptions() {
        context.checking(new Expectations() {{
            allowing (mock).doSomething(); will(throwException(new ExampleException()));
        }});
        
        try {
            mock.doSomething();
            fail("no exception thrown");
        }
        catch (ExampleException expected) {}
        
        assertThat(asString(context), containsInOrder(
            "what happened:",
            "mock.doSomething()"));
    }

    public void testDoesNotRecordUnexpectedInvocations() {
        context.checking(new Expectations() {{
            allowing (mock).doSomethingWith("foo");
        }});
        
        try {
            mock.doSomethingWith("bar");
        }
        catch (ExpectationError expected) {}
        
        assertThat(asString(context), containsInOrder(
            "what happened:", 
            "nothing"));
    }
}
