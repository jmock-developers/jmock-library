/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.framework;

import junit.framework.TestCase;
import test.jmock.dynamic.testsupport.MockInvokable;

import java.lang.reflect.Method;

import org.jmock.dynamic.framework.DynamicMockError;
import org.jmock.dynamic.framework.Invocation;
import org.jmock.dynamic.framework.LIFOInvocationDispatcher;

public class LIFOInvocationDispatcherTest extends TestCase {

    private Invocation invocation;
    private LIFOInvocationDispatcher dispatcher;
    private MockInvokable invokable1 = new MockInvokable();
    private MockInvokable invokable2 = new MockInvokable();

    public void setUp() throws NoSuchMethodException {
        invocation = new Invocation(getDummyMethod(), null);
        dispatcher = new LIFOInvocationDispatcher();
    }

    public void dummyMethod() {
    };
    
    public void testInvokeFailsWhenEmpty() throws Throwable {
        try {
            dispatcher.dispatch(invocation);
        } catch (DynamicMockError ex) {
            assertSame("should be same invocation", invocation, ex.invocation);
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
        } catch (Throwable t) {
            assertSame("should be same exception", exception, t);
        }
    }

    public void testInvokeFailsWhenNoInvokablesMatch() throws Throwable {
        invokable1.matchesResult = false;
        invokable2.matchesResult = false;

        dispatcher.add(invokable1);
        dispatcher.add(invokable2);

        try {
            dispatcher.dispatch(invocation);
        } catch (DynamicMockError ex) {
            assertSame("should be same invocation", invocation, ex.invocation);
            return;
        }
        fail("expected AssertionFailedError");
    }

    public void testLaterInvokablesOverrideEarlierInvokables() throws Throwable {
        invokable1.matchesInvocation.setExpectNothing();
        invokable1.matchesResult = true;
        invokable1.invokeInvocation.setExpectNothing();

        invokable2.matchesInvocation.setExpected(invocation);
        invokable2.matchesResult = true;
        invokable2.invokeInvocation.setExpected(invocation);


        dispatcher.add(invokable1);
        dispatcher.add(invokable2);

        dispatcher.dispatch(invocation);

        verifyInvokables();
    }

    public void testSearchesForMatchInLIFOOrder() throws Throwable {
        invokable1.matchesInvocation.setExpected(invocation);
        invokable1.matchesResult = true;
        invokable1.invokeInvocation.setExpected(invocation);
        invokable1.invokeResult = "one";

        invokable2.matchesInvocation.setExpected(invocation);
        invokable2.matchesResult = false;
        invokable2.invokeInvocation.setExpectNothing();


        dispatcher.add(invokable1);
        dispatcher.add(invokable2);

        assertEquals("Should be invokable1", "one", dispatcher.dispatch(invocation));

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

    private Method getDummyMethod() throws NoSuchMethodException {
        return getClass().getDeclaredMethod("dummyMethod", new Class[0]);
    }

    private void verifyInvokables() {
        invokable1.verifyExpectations();
        invokable2.verifyExpectations();
    }
}
