/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.Mock;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.Invokable;
import org.jmock.expectation.AssertMo;

import test.jmock.builder.testsupport.MockMatchBuilder;
import test.jmock.core.DummyInterface;
import test.jmock.core.testsupport.*;


public class MockTest extends TestCase
{
    private static final String MOCK_NAME = "Test Mock Name";

    private MockDynamicMock mockCoreMock = new MockDynamicMock();
    private MockInvocationDispatcher mockDispatcher = new MockInvocationDispatcher();
    private Mock mock = new Mock(mockCoreMock, MOCK_NAME, mockDispatcher);

    public void testToStringComesFromUnderlyingDynamicMock() {
        mockCoreMock.toStringResult = "some string here";
        assertEquals("Should be same string", "some string here", mock.toString());
    }

    public void testPassesExplicitNameToCoreMock() {
        String explicitName = "EXPLICIT NAME";

        assertEquals("should be explicit name", explicitName,
                     new Mock(DummyInterface.class, explicitName).toString());
    }

    public void testDelegatgesResetToCoreMock() {
        mockCoreMock.resetCalls.setExpected(1);

        mock.reset();

        mockCoreMock.verifyExpectations();
    }

    public void testDelegatesAddInvokableToDispatcher() {
        Invokable invokable = new MockInvokable();

        mockDispatcher.addInvokable.setExpected(invokable);

        mock.addInvokable(invokable);

        mockDispatcher.verifyExpectations();
    }

    public void testStubAddsInvocationMockerAndReturnsBuilder() {
        mockDispatcher.addCalls.setExpected(1);

        assertNotNull("Should be invokedMethod expectation", mock.stubs());
        mockDispatcher.verifyExpectations();
    }

    public void testExpectsAddsInvocationMockerAndAddsExpectationAndReturnsBuilder() {
        InvocationMatcher expectation = new MockInvocationMatcher();

        mockDispatcher.addCalls.setExpected(1);

        assertNotNull("Should be invokedMethod expectation", mock.expects(expectation));
        mockDispatcher.verifyExpectations();
    }

    public void testVerifyCallsUnderlyingDispatcher() {
        mockDispatcher.verifyCalls.setExpected(1);

        mock.verify();

        mockDispatcher.verifyExpectations();
    }

    public void testVerifyFailuresIncludeMockName() {
        mockDispatcher.verifyFailure = new AssertionFailedError("verify failure");

        mockDispatcher.verifyCalls.setExpected(1);

        try {
            mock.verify();
        }
        catch (AssertionFailedError expected) {
            AssertMo.assertIncludes("Should include mock name", MOCK_NAME, expected.getMessage());
            mockDispatcher.verifyExpectations();
            return;
        }
        fail("Should have thrown exception");
    }

    private interface MockedType
    {
    }

    public void testReportsTypesMockedByUnderlyingMock() {
        mockCoreMock.getMockedTypeCalls.setExpected(1);
        mockCoreMock.getMockedTypeResult = MockedType.class;

        AssertMo.assertSame("mocked types",
                            MockedType.class, mock.getMockedType());
    }

    public void testPassesDefaultStubToDispatcher() {
        MockStub mockDefaultStub = new MockStub();

        mockDispatcher.setDefaultStub.setExpected(mockDefaultStub);

        mock.setDefaultStub(mockDefaultStub);

        mockDispatcher.verifyExpectations();
    }


    static final String BUILDER_ID = "BUILDER-ID";

    public void testStoresExpectationBuildersByID() {
        MockMatchBuilder builder1 = new MockMatchBuilder();
        MockMatchBuilder builder2 = new MockMatchBuilder();

        mock.registerUniqueID(BUILDER_ID + 1, builder1);
        mock.registerUniqueID(BUILDER_ID + 2, builder2);

        assertSame("should be builder1",
                   builder1, mock.lookupID(BUILDER_ID + 1));
        assertSame("should be builder2",
                   builder2, mock.lookupID(BUILDER_ID + 2));
    }

    public void testFailsOnLookingUpUnregisteredID() {
        try {
            mock.lookupID(BUILDER_ID);
        }
        catch (AssertionFailedError ex) {
            assertTrue("error message should contain invalid id",
                       ex.getMessage().indexOf(BUILDER_ID) >= 0);
            return;
        }
        fail("expected AssertionFailedError");
    }

    public void testMethodNamesCanBeUsedToNameBuildersInAdditionToUniqueID() {
        MockMatchBuilder builder1 = new MockMatchBuilder();
        MockMatchBuilder builder2 = new MockMatchBuilder();

        mock.registerMethodName(BUILDER_ID + 1, builder1);
        mock.registerMethodName(BUILDER_ID + 2, builder2);

        assertSame("should be builder1",
                   builder1, mock.lookupID(BUILDER_ID + 1));
        assertSame("should be builder2",
                   builder2, mock.lookupID(BUILDER_ID + 2));
    }

    public void testDuplicateMethodNameOverridesPreviousMapping() {
        MockMatchBuilder builder1 = new MockMatchBuilder();
        MockMatchBuilder builder2 = new MockMatchBuilder();

        mock.registerMethodName(BUILDER_ID, builder1);
        mock.registerMethodName(BUILDER_ID, builder2);

        assertSame("builder2", builder2, mock.lookupID(BUILDER_ID));
    }

    public void testDuplicateUniqueIDCausesTestFailure() {
        MockMatchBuilder builder1 = new MockMatchBuilder();
        MockMatchBuilder builder2 = new MockMatchBuilder();

        mock.registerUniqueID(BUILDER_ID, builder1);
        try {
            mock.registerUniqueID(BUILDER_ID, builder2);
        }
        catch (AssertionFailedError ex) {
            AssertMo.assertIncludes("should contain invalid ID",
                                    BUILDER_ID, ex.getMessage());
            return;
        }

        fail("expected failure");
    }
}

