/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import test.jmock.dynamic.testsupport.MockInvocationDispatcher;
import test.jmock.dynamic.testsupport.MockInvokable;

import org.jmock.dynamic.CoreMock;
import org.jmock.dynamic.DynamicUtil;
import org.jmock.dynamic.Invocation;
import org.jmock.expectation.AssertMo;


public class CoreMockTest extends TestCase {
    private static final String MOCK_NAME = "Test coreMock";

    private DummyInterface proxy;
    private CoreMock coreMock;
    private MockInvocationDispatcher mockDispatcher = new MockInvocationDispatcher();
    private MockInvokable mockInvokable = new MockInvokable();

    public void setUp() {
        coreMock = new CoreMock(DummyInterface.class, MOCK_NAME, mockDispatcher);

        try {
            proxy = (DummyInterface) coreMock.proxy();
        } catch (ClassCastException ex) {
            fail("proxy is not of expected interface type");
        }
    }

    public void testMockAnnotatesAssertionFailedError()
            throws Throwable {
        final String originalMessage = "original message";

        Object arg = new AssertionFailedError(originalMessage);
        mockDispatcher.dispatchResult = arg;

        try {
            proxy.noArgVoidMethod();
        } catch (AssertionFailedError err) {
            AssertMo.assertIncludes("should contain original message", originalMessage, err.getMessage());
            AssertMo.assertIncludes("should contain coreMock name", MOCK_NAME, err.getMessage());
        }
    }

    public void testProxyReturnsConfiguredResult() throws Throwable {
        final String RESULT = "configured result";

        mockDispatcher.dispatchResult = RESULT;

        assertSame("result is returned by coreMock", RESULT, proxy.oneArgMethod("arg"));
    }

    public void testExceptionsPropagatedThroughProxy() throws Throwable {
        final Throwable throwable = new DummyThrowable();

        mockDispatcher.dispatchThrowable = throwable;

        try {
            proxy.noArgVoidMethod();
        } catch (Throwable ex) {
            assertSame("exception is caught by coreMock", throwable, ex);
            return;
        }
        fail("Should have thrown exception");
    }

    public void testMockVerifies() throws Exception {
        mockDispatcher.verifyCalls.setExpected(1);

        coreMock.verify();

        // Can't use Verifier as we are verifying "verify"
        mockDispatcher.verifyExpectations();
    }

    public void testProxyEquality() throws Exception {
        mockDispatcher.dispatchResult = new Boolean(false);

        mockDispatcher.dispatchInvocation.setExpectNothing();

        assertTrue( "Proxy equality is implemented directly", proxy.equals(proxy));
        mockDispatcher.verifyExpectations();
    }

    public void testProxyInequality() throws Exception {
        mockDispatcher.dispatchResult = new Boolean(false);

        mockDispatcher.dispatchInvocation.setExpected(
                new Invocation(Void.class, "equals", new Class[]{Object.class}, boolean.class,
                        new Object[]{"not a proxy"}));

        assertFalse( "Should handle proxy inequality by calling through", 
                     proxy.equals("not a proxy") );
        mockDispatcher.verifyExpectations();
    }

    public void testProxyEqualityWithNull() throws Exception {
        mockDispatcher.dispatchResult = new Boolean(true);
        mockDispatcher.dispatchInvocation.setExpected(
                new Invocation(Void.class, "equals", new Class[]{Object.class}, boolean.class,
                        new Object[]{null}));

        assertTrue("Proxy should handle null equality", proxy.equals(null));
        mockDispatcher.verifyExpectations();
    }

    public void testGeneratesMockNameFromInterfaceNameIfNoNameSpecified() throws Exception {
        assertEquals("mockString", CoreMock.mockNameFromClass(String.class));
    }

    public void testResultOfToStringContainsName() {
        AssertMo.assertIncludes( "result of toString() should include name", 
        						 MOCK_NAME, coreMock.toString());
    }

    public void testProxyToString() throws Exception {
        assertEquals( "Should get a coreMock name without touching the underlying coreMock", 
    				  MOCK_NAME, DynamicUtil.toReadableString(proxy));
        coreMock.verify();  // should not fail on a proxyToString call
    }

    public void testAddAnInvokable() {
        mockDispatcher.addInvokable.setExpected(mockInvokable);

        coreMock.add(mockInvokable);

        mockDispatcher.verifyExpectations();
    }


    public void testReset() {
        mockDispatcher.clearCalls.setExpected(1);

        coreMock.reset();

        mockDispatcher.verifyExpectations();
    }

    public void testVerifyFailuresIncludeMockName() {
        mockDispatcher.verifyFailure = new AssertionFailedError("verify failure");

        mockDispatcher.verifyCalls.setExpected(1);

        try {
            coreMock.verify();
        } catch (AssertionFailedError expected) {
            AssertMo.assertIncludes("Should include mock name", MOCK_NAME, expected.getMessage());
            mockDispatcher.verifyExpectations();
            return;
        }
        fail("Should have thrown exception");
    }
}
