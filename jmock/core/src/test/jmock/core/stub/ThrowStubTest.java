/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.core.stub;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.core.Invocation;
import org.jmock.core.stub.ThrowStub;
import org.jmock.expectation.AssertMo;
import test.jmock.core.DummyThrowable;
import test.jmock.core.testsupport.MethodFactory;


public class ThrowStubTest
        extends TestCase
{
    static final Throwable THROWABLE = new DummyThrowable();

    static final Class[] EXCEPTION_TYPES = {DummyThrowable.class};

    MethodFactory methodFactory;
    Invocation invocation;
    ThrowStub throwStub;

    public void setUp() {
        methodFactory = new MethodFactory();
        invocation = new Invocation("INVOKED-OBJECT",
                                    methodFactory.newMethod("methodName", MethodFactory.NO_ARGUMENTS, void.class, EXCEPTION_TYPES),
                                    null);
        throwStub = new ThrowStub(THROWABLE);
    }

    public void testThrowsThrowableObjectPassedToConstructorWhenInvoked() {
        try {
            throwStub.invoke(invocation);
        }
        catch (Throwable t) {
            assertSame("Should be the same throwable", THROWABLE, t);
        }
    }

    public void testIncludesDetailsOfThrowableInDescription() {
        StringBuffer buffer = new StringBuffer();

        throwStub.describeTo(buffer);

        String description = buffer.toString();

        assertTrue("contains class of thrown object in description",
                   description.indexOf(THROWABLE.toString()) >= 0);
        assertTrue("contains 'throws' in description",
                   description.indexOf("throws") >= 0);
    }

    public static class ExpectedExceptionType1 extends Exception
    {
    }

    public static class ExpectedExceptionType2 extends Exception
    {
    }

    public void testThrowsAssertionFailedErrorIfTriesToThrowIncompatibleCheckedException()
            throws Throwable {
        Class[] expectedExceptionTypes = {ExpectedExceptionType1.class, ExpectedExceptionType2.class};
        Invocation incompatibleInvocation = new Invocation("INVOKED-OBJECT",
                                                           methodFactory.newMethod("methodName", MethodFactory.NO_ARGUMENTS, void.class, expectedExceptionTypes),
                                                           null);

        try {
            throwStub.invoke(incompatibleInvocation);
        }
        catch (AssertionFailedError ex) {
            String message = ex.getMessage();

            for (int i = 0; i < expectedExceptionTypes.length; i++) {
                AssertMo.assertIncludes("should include name of expected exception types",
                                        expectedExceptionTypes[i].getName(), message);
            }
            AssertMo.assertIncludes("should include name of thrown exception type",
                                    THROWABLE.getClass().getName(), message);
            return;
        }
        fail("should have failed");
    }

    public void testGivesInformativeErrorMessageIfAttemptToThrowCheckedExceptionFromMethodWithNoExceptions()
            throws Throwable {
        Invocation incompatibleInvocation = new Invocation("INVOKED-OBJECT",
                                                           methodFactory.newMethod("methodName", MethodFactory.NO_ARGUMENTS, void.class, MethodFactory.NO_EXCEPTIONS),
                                                           null);

        try {
            throwStub.invoke(incompatibleInvocation);
        }
        catch (AssertionFailedError ex) {
            String message = ex.getMessage();

            AssertMo.assertIncludes("should include name of thrown exception type",
                                    THROWABLE.getClass().getName(), message);
            AssertMo.assertIncludes("should describe that the method doesn't allow any exceptions",
                                    "no exceptions", message);
            return;
        }
        fail("should have failed");
    }

    public void testDoesNotCheckTypeCompatiblityOfUncheckedExceptions() throws Throwable {
        throwStub = new ThrowStub(new RuntimeException());

        try {
            throwStub.invoke(invocation);
        }
        catch (RuntimeException ex) {
            return;
        }
        fail("should have thrown a RuntimeException");
    }

    public void testDoesNotCheckTypeCompatiblityOfErrors() throws Throwable {
        throwStub = new ThrowStub(new Error());

        try {
            throwStub.invoke(invocation);
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
            throwStub.invoke(invocation);
        }
        catch (Throwable t) {
            StackTraceElement[] stackTrace = t.getStackTrace();

            assertEquals("thrown from ThrowStub object",
                         throwStub.getClass().getName(), stackTrace[0].getClassName());
        }
    }
}
