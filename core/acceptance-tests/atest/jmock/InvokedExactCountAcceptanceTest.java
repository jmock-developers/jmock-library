package atest.jmock;

import junit.framework.AssertionFailedError;
import org.jmock.MockObjectTestCase;
import org.jmock.Mock;


public class InvokedExactCountAcceptanceTest extends MockObjectTestCase
{
    interface MockedType {
        void m();
    }

    public void testCalledExactNumberOfTimes() {
        Mock mock = mock(MockedType.class,"mock");
        MockedType proxy = (MockedType)mock.proxy();

        mock.expects(exactly(2)).method("m").withNoArguments();

        proxy.m();

        try {
            mock.verify();
            throw new Error("AssertionFailedError expected");
        }
        catch( AssertionFailedError err ) {
            // expected
        }

        proxy.m();

        try {
            proxy.m();
            throw new Error("AssertionFailedError expected");
        }
        catch( AssertionFailedError err ) {
            // expected
        }
    }
}
