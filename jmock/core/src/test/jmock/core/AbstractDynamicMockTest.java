/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.core.*;
import org.jmock.expectation.AssertMo;

import test.jmock.core.testsupport.MockInvocationDispatcher;


public abstract class AbstractDynamicMockTest extends TestCase
{
    private static final String MOCK_NAME = "Test coreMock";

    private DummyInterface proxy;
    private DynamicMock coreMock;
    private MockInvocationDispatcher mockDispatcher = new MockInvocationDispatcher();

    public void setUp() {
        coreMock = createDynamicMock( MOCK_NAME, mockDispatcher );

        try {
            proxy = (DummyInterface)coreMock.proxy();
        }
        catch (ClassCastException ex) {
            fail("proxy is not of expected interface type");
        }
    }

    protected abstract DynamicMock createDynamicMock( String name, InvocationDispatcher dispatcher );
    protected abstract Class mockedType();

    protected DummyInterface proxy() {
        return proxy;
    }

    public void testReportsMockedType() {
        assertSame("mocked type",
                   mockedType(), coreMock.getMockedType());
    }

    public void testMockAnnotatesAssertionFailedError()
            throws Throwable {
        final String originalMessage = "original message";

        Throwable throwable = new AssertionFailedError(originalMessage);
        mockDispatcher.dispatchThrowable = throwable;

        try {
            proxy.noArgVoidMethod();
            fail("should throw AssertionFailedError");
        }
        catch (AssertionFailedError err) {
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
        }
        catch (Throwable ex) {
            assertSame("exception is caught by coreMock", throwable, ex);
            return;
        }
        fail("Should have thrown exception");
    }

    public void testTestsEqualityForProxy() throws Exception {
        coreMock = createDynamicMock( "coreMock", new OrderedInvocationDispatcher.LIFO());
        proxy = (DummyInterface)coreMock.proxy();

        assertTrue("should be equal", proxy.equals(proxy));
        assertFalse("should not be equal", proxy.equals(new Object()));
        assertFalse("shuold not be equal to null", proxy.equals(null));
    }

    public void testCanOverrideEqualsForProxyBySettingAStub() throws Exception {
        mockDispatcher.dispatchResult = new Boolean(false);

        mockDispatcher.dispatchInvocation.setExpected(new Invocation(proxy,
                                                                     Object.class.getMethod("equals", new Class[]{Object.class}),
                                                                     new Object[]{"not a proxy"}));

        assertFalse("Passes invocation of equals to dispatcher",
                    proxy.equals("not a proxy"));

        mockDispatcher.verifyExpectations();
    }

    public void testCalculatesHashCodeForProxy() throws Exception {
        coreMock = new CoreMock(DummyInterface.class, "coreMock", new OrderedInvocationDispatcher.LIFO());

        proxy = (DummyInterface)coreMock.proxy();

        assertEquals("same hash code", proxy.hashCode(), proxy.hashCode());
    }

    public void testCanOverrideHashCodeForProxyBySettingAStub() throws Exception {
        int expectedHashCode = 1;

        mockDispatcher.dispatchResult = new Integer(expectedHashCode);
        mockDispatcher.dispatchInvocation.setExpected(new Invocation(proxy, Object.class.getMethod("hashCode", new Class[0]),
                                                                     new Object[0]));

        assertEquals("proxy hashCode", expectedHashCode, proxy.hashCode());

        mockDispatcher.verifyExpectations();
    }

    public void testGeneratesMockNameFromInterfaceNameIfNoNameSpecified() throws Exception {
        assertEquals("mockString", CoreMock.mockNameFromClass(String.class));
    }

    public void testReturnsNameFromToString() {
        AssertMo.assertIncludes("result of toString() should include name",
                                MOCK_NAME, coreMock.toString());
    }
}
