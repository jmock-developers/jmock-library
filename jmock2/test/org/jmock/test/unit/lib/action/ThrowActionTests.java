/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.test.unit.lib.action;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.hamcrest.StringDescription;
import org.jmock.api.Invocation;
import org.jmock.lib.action.ThrowAction;
import org.jmock.test.unit.support.AssertThat;
import org.jmock.test.unit.support.DummyThrowable;
import org.jmock.test.unit.support.MethodFactory;


public class ThrowActionTests extends TestCase {
    static final Throwable THROWABLE = new DummyThrowable();
    static final Class<?>[] EXCEPTION_TYPES = {DummyThrowable.class};

    MethodFactory methodFactory;
    Invocation invocation;
    ThrowAction throwAction;

    @Override
    public void setUp() {
        methodFactory = new MethodFactory();
        invocation = new Invocation("INVOKED-OBJECT",
                                    methodFactory.newMethod("methodName", MethodFactory.NO_ARGUMENTS, void.class, EXCEPTION_TYPES));
        throwAction = new ThrowAction(THROWABLE);
    }

    public void testThrowsThrowableObjectPassedToConstructorWhenInvoked() {
        try {
            throwAction.invoke(invocation);
        }
        catch (Throwable t) {
            assertSame("Should be the same throwable", THROWABLE, t);
        }
    }

    public void testIncludesDetailsOfThrowableInDescription() {
    	String description = StringDescription.toString(throwAction);

        assertTrue("contains class of thrown object in description",
                   description.indexOf(THROWABLE.toString()) >= 0);
        assertTrue("contains 'throws' in description",
                   description.indexOf("throws") >= 0);
    }

    public static class ExpectedExceptionType1 extends Exception {
        private static final long serialVersionUID = 1L;
    }

    public static class ExpectedExceptionType2 extends Exception {
        private static final long serialVersionUID = 1L;
    }

    public void testDoesNotAllowThrowingIncompatibleCheckedException() throws Throwable {
        Class<?>[] expectedExceptionTypes = {ExpectedExceptionType1.class, ExpectedExceptionType2.class};
        Invocation incompatibleInvocation = 
            new Invocation("INVOKED-OBJECT", methodFactory.newMethod("methodName", MethodFactory.NO_ARGUMENTS, void.class, expectedExceptionTypes));

        try {
            throwAction.invoke(incompatibleInvocation);
        }
        catch (IllegalStateException ex) {
            String message = ex.getMessage();

            for (int i = 0; i < expectedExceptionTypes.length; i++) {
                AssertThat.stringIncludes("should include name of expected exception types",
                    expectedExceptionTypes[i].getName(), message);
            }
            AssertThat.stringIncludes("should include name of thrown exception type",
                THROWABLE.getClass().getName(), message);
            return;
        }
        fail("should have failed");
    }

    public void testGivesInformativeErrorMessageIfAttemptToThrowCheckedExceptionFromMethodWithNoExceptions() throws Throwable {
        Invocation incompatibleInvocation = 
            new Invocation("INVOKED-OBJECT", methodFactory.newMethod("methodName", MethodFactory.NO_ARGUMENTS, void.class, MethodFactory.NO_EXCEPTIONS));
        
        try {
            throwAction.invoke(incompatibleInvocation);
        }
        catch (IllegalStateException ex) {
            String message = ex.getMessage();

            AssertThat.stringIncludes("should include name of thrown exception type",
                THROWABLE.getClass().getName(), message);
            AssertThat.stringIncludes("should describe that the method doesn't allow any exceptions",
                "no exceptions", message);
            return;
        }
        fail("should have failed");
    }

    public void testDoesNotCheckTypeCompatiblityOfUncheckedExceptions() throws Throwable {
        throwAction = new ThrowAction(new RuntimeException());

        try {
            throwAction.invoke(invocation);
        }
        catch (RuntimeException ex) {
            return;
        }
        fail("should have thrown a RuntimeException");
    }

    public void testDoesNotCheckTypeCompatiblityOfErrors() throws Throwable {
        throwAction = new ThrowAction(new Error());

        try {
            throwAction.invoke(invocation);
        }
        catch (AssertionFailedError err) {
            throw err;
        }
        catch (Error ex) {
            return;
        }
        fail("should have thrown an Error");
    }

    public void testSetsStackTraceWhenExceptionIsThrown() {
        try {
            throwAction.invoke(invocation);
        }
        catch (Throwable t) {
            StackTraceElement[] stackTrace = t.getStackTrace();

            assertEquals("thrown from ThrowAction object",
                         throwAction.getClass().getName(), stackTrace[0].getClassName());
        }
    }
}
