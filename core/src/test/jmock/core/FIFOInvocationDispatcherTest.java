/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core;

import java.lang.reflect.Method;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.jmock.core.Invocation;
import org.jmock.core.FIFOInvocationDispatcher;
import org.jmock.util.Dummy;
import test.jmock.core.testsupport.MockInvokable;
import test.jmock.core.testsupport.MockStub;


public class FIFOInvocationDispatcherTest extends TestCase
{
    private Invocation invocation;
    private FIFOInvocationDispatcher dispatcher;
    private MockInvokable invokable1 = new MockInvokable();
    private MockInvokable invokable2 = new MockInvokable();

    public void setUp() throws NoSuchMethodException {
        invocation = new Invocation("INVOKED-OBJECT", getDummyMethod(), null);
        dispatcher = new FIFOInvocationDispatcher();
    }

    public void dummyMethod() { /* just used to create Invocation objects */
    }

    public void testInvokeFailsWhenEmpty() throws Throwable {
        try {
            dispatcher.dispatch(invocation);
        }
        catch (AssertionFailedError expected) {
            return;
        }
        fail("expected AssertionFailedError");
    }

    public void testInvokesInvokableThatMatches() throws Throwable {
        Object result = "invoke result";

        invokable1.matchesInvocation.setExpected(invocation);
        invokable1.matchesResult = true;
        invokable1.invokeInvocation.setExpected(invocation);
        invokable1.invokeResult = result;

        dispatcher.add(invokable1);
        dispatcher.dispatch(invocation);

        invokable1.verifyExpectations();
    }

    public void testReturnsValueFromInvokable() throws Throwable {
        Object result = "invoke result";

        invokable1.matchesResult = true;
        invokable1.invokeResult = result;

        dispatcher.add(invokable1);

        assertSame("should be same result", result, dispatcher.dispatch(invocation));
    }

    public void testPropagatesExceptionFromInvokable() throws Throwable {
        Throwable exception = new Throwable("test throwable");

        invokable1.matchesResult = true;
        invokable1.invokeThrow = exception;

        dispatcher.add(invokable1);

        try {
            dispatcher.dispatch(invocation);
            fail("expected exception");
        }
        catch (Throwable t) {
            assertSame("should be same exception", exception, t);
        }
    }

    public void testByDefaultInvokeFailsWhenNoInvokablesMatch() throws Throwable {
        invokable1.matchesResult = false;
        invokable2.matchesResult = false;

        dispatcher.add(invokable1);
        dispatcher.add(invokable2);

        try {
            dispatcher.dispatch(invocation);
        }
        catch (AssertionFailedError ex) {
            return;
        }
        fail("expected AssertionFailedError");
    }

    public void testEarlierInvokablesOverrideLaterInvokables() throws Throwable {
        invokable1.matchesInvocation.setExpected(invocation);
        invokable1.matchesResult = true;
        invokable1.invokeInvocation.setExpected(invocation);

        invokable2.matchesInvocation.setExpectNothing();
        invokable2.matchesResult = true;
        invokable2.invokeInvocation.setExpectNothing();

        dispatcher.add(invokable1);
        dispatcher.add(invokable2);

        dispatcher.dispatch(invocation);

        verifyInvokables();
    }

    public void testSearchesForMatchInFIFOOrder() throws Throwable {
        invokable1.matchesInvocation.setExpected(invocation);
        invokable1.matchesResult = false;
        invokable1.invokeInvocation.setExpectNothing();

        invokable2.matchesInvocation.setExpected(invocation);
        invokable2.matchesResult = true;
        invokable2.invokeInvocation.setExpected(invocation);
        invokable2.invokeResult = "two";

        dispatcher.add(invokable1);
        dispatcher.add(invokable2);

        assertEquals("Should be invokable2", "two", dispatcher.dispatch(invocation));

        verifyInvokables();
    }

    public void testVerifiesAllInvokables() {
        invokable1.verifyCalls.setExpected(1);
        invokable2.verifyCalls.setExpected(1);

        dispatcher.add(invokable1);
        dispatcher.add(invokable2);

        dispatcher.verify();

        verifyInvokables();
    }

    public void testClearRemovesAllInvokables() throws Throwable {
        invokable1.matchesResult = true;

        dispatcher.add(invokable1);

        dispatcher.clear();
        testInvokeFailsWhenEmpty();
    }

    public void testInvokesDefaultStubWhenNoInvokablesMatch() throws Throwable {
        MockStub mockDefaultStub = new MockStub("mockDefaultStub");
        Object defaultStubResult = Dummy.newDummy("DEFAULT STUB RESULT");

        dispatcher.add(invokable1);
        dispatcher.add(invokable2);

        invokable1.matchesResult = false;
        invokable2.matchesResult = false;

        mockDefaultStub.invokeInvocation.setExpected(invocation);
        mockDefaultStub.invokeResult = defaultStubResult;

        dispatcher.setDefaultStub(mockDefaultStub);
        assertSame("should be result of calling default stub",
                   defaultStubResult, dispatcher.dispatch(invocation));

        verifyInvokables();
        mockDefaultStub.verify();
    }

    private Method getDummyMethod() throws NoSuchMethodException {
        return getClass().getDeclaredMethod("dummyMethod", new Class[0]);
    }

    private void verifyInvokables() {
        invokable1.verifyExpectations();
        invokable2.verifyExpectations();
    }
}
