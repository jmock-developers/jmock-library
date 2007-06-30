package org.jmock.test.acceptance;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class ThrowingExceptionsAcceptanceTests extends TestCase {
    public static class CheckedException extends Exception {}
    public static class UncheckedException extends RuntimeException {}
    public static class AnError extends Error {}
    public static class IncompatibleCheckedException extends Exception {}
    
    public interface Throwing {
        void doSomething() throws CheckedException;
    }

    Mockery context = new Mockery();
    Throwing mock = context.mock(Throwing.class, "mock");
    
    public void testCanThrowCheckedExceptions() throws Exception {
        context.checking(new Expectations() {{
            allowing (mock).doSomething(); will(throwException(new CheckedException()));
        }});
        
        try {
            mock.doSomething();
            fail("should have thrown CheckedException");
        }
        catch (CheckedException ex) {
            // expected
        }
    }
    
    public void testCanThrowUncheckedExceptions() throws Exception {
        context.checking(new Expectations() {{
            allowing (mock).doSomething(); will(throwException(new UncheckedException()));
        }});
        
        try {
            mock.doSomething();
            fail("should have thrown UncheckedException");
        }
        catch (UncheckedException ex) {
            // expected
        }
    }
    
    public void testCanThrowErrors() throws Exception {
        context.checking(new Expectations() {{
            allowing (mock).doSomething(); will(throwException(new AnError()));
        }});
        
        try {
            mock.doSomething();
            fail("should have thrown AnError");
        }
        catch (AnError ex) {
            // expected
        }
    }
    
    public void testCannotThrowUndeclaredCheckedExceptions() throws Exception {
        context.checking(new Expectations() {{
            allowing (mock).doSomething(); will(throwException(new IncompatibleCheckedException()));
        }});
        
        try {
            mock.doSomething();
            fail("should have thrown IllegalStateException");
        }
        catch (IllegalStateException ex) {
            // expected
        }
    }
}
