/*  Copyright (c) 2000-2004 jMock.org
 */
package atest.jmock;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.DynamicMockError;


public class ExpectNeverAcceptanceTest
        extends MockObjectTestCase
{
    private static interface MockedInterface
    {
        public void method();
    }

    public void testExpectNotCalledOverridesStubAndFailsIfCalled() {
        Mock mock = mock(MockedInterface.class, "mock");

        mock.stubs().method("invokedMethod").withNoArguments();
        mock.expects(never()).method("invokedMethod").withNoArguments();

        try {
            ((MockedInterface)mock.proxy()).method();
        }
        catch (DynamicMockError error) {
            return;
        }
        fail("DynamicMockError expected");
    }

    public void testExpectNotCalledVerifiesIfNotCalled() {
        Mock mock = mock(MockedInterface.class, "mock");

        mock.stubs().method("invokedMethod").withNoArguments().isVoid();
        mock.expects(never()).method("invokedMethod").withNoArguments();
    }
}
