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
import test.jmock.core.testsupport.MockDynamicMock;
import test.jmock.core.testsupport.MockInvocationMatcher;
import test.jmock.core.testsupport.MockInvokable;
import test.jmock.core.testsupport.MockStub;


public class MockTest extends TestCase
{

    private MockDynamicMock mockCoreMock = new MockDynamicMock();
    private Mock mock = new Mock(mockCoreMock);

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

    public void testDelegatesAddInvokableToCoreMock() {
        Invokable invokable = new MockInvokable();

        mockCoreMock.addInvokableCalls.setExpected(1);
        mockCoreMock.addInvokable.setExpected(invokable);

        mock.addInvokable(invokable);

        mockCoreMock.verifyExpectations();
    }

    public void testStubAddsInvocationMockerAndReturnsBuilder() {
        mockCoreMock.addInvokableCalls.setExpected(1);

        assertNotNull("Should be invokedMethod expectation", mock.stubs());
        mockCoreMock.verifyExpectations();
    }

    public void testExpectAddsInvocationMockerAndAddsExpectationAndReturnsBuilder() {
        InvocationMatcher expectation = new MockInvocationMatcher();

        mockCoreMock.addInvokableCalls.setExpected(1);

        assertNotNull("Should be invokedMethod expectation", mock.expects(expectation));
        mockCoreMock.verifyExpectations();
    }

    public void testVerifyCallsUnderlyingMock() {
        mockCoreMock.verifyCalls.setExpected(1);

        mock.verify();

        mockCoreMock.verifyExpectations();
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

    public void testPassesDefaultStubToCoreMock() {
        MockStub mockDefaultStub = new MockStub();

        mockCoreMock.setDefaultStub.setExpected(mockDefaultStub);

        mock.setDefaultStub(mockDefaultStub);

        mockCoreMock.verifyExpectations();
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

    public void testClearsIDsWhenReset() {
        MockMatchBuilder builder1 = new MockMatchBuilder();
        MockMatchBuilder builder2 = new MockMatchBuilder();

        mock.registerUniqueID(BUILDER_ID, builder1);

        mock.reset();

        mock.registerUniqueID(BUILDER_ID, builder2);
    }
}

