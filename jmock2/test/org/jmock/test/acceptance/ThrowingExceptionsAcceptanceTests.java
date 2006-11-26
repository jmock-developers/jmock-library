package org.jmock.test.acceptance;

import org.jmock.InAnyOrder;
import org.jmock.Mockery;

import junit.framework.TestCase;

public class ThrowingExceptionsAcceptanceTests extends TestCase {
    public class CheckedException extends Exception {}
    public class UncheckedException extends RuntimeException {}
    public class AnError extends Error {}
    public class IncompatibleCheckedException extends Exception {}
    
    public interface Throwing {
        void doSomething() throws CheckedException;
    }

    Mockery context = new Mockery();
    Throwing mock = context.mock(Throwing.class, "mock");
    
    public void testCanThrowCheckedExceptions() throws Exception {
        context.expects(new InAnyOrder() {{
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
        context.expects(new InAnyOrder() {{
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
        context.expects(new InAnyOrder() {{
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
        context.expects(new InAnyOrder() {{
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
