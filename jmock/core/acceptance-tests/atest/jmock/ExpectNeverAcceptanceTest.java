/*  Copyright (c) 2000-2004 jMock.org
 */
package atest.jmock;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.expectation.AssertMo;
import org.jmock.core.DynamicMockError;


public class ExpectNeverAcceptanceTest
        extends MockObjectTestCase
{
    private static interface MockedInterface {
        public void method();
    }

    public void testExpectNeverOverridesStubAndFailsIfCalled() {
        Mock mock = mock(MockedInterface.class, "mock");

        mock.stubs().method("method").withNoArguments();
        mock.expects(never()).method("method").withNoArguments();

        try {
            ((MockedInterface)mock.proxy()).method();
        }
        catch (DynamicMockError error) {
            return;
        }
        fail("DynamicMockError expected");
    }

    public void testExpectNeverVerifiesIfNotCalled() {
        Mock mock = mock(MockedInterface.class, "mock");

        mock.stubs().method("method").withNoArguments().isVoid();
        mock.expects(never()).method("method").withNoArguments();
    }

    public void testExpectNeverCanExplicitlyDescribeError() {
        Mock mock = mock(MockedInterface.class,"mock");
        String errorMessage = "errorMessage";

        mock.expects(never(errorMessage)).method("method").withNoArguments();
        mock.expects(never(errorMessage)).method("method").withNoArguments();

        try {
            ((MockedInterface)mock.proxy()).method();
        }
        catch (DynamicMockError error) {
            AssertMo.assertIncludes( "should contain explicit error message", errorMessage, error.getMessage() );
            return;
        }
        fail("DynamicMockError expected");
    }
}
