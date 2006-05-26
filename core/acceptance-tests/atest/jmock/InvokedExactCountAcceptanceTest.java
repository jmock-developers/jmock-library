package atest.jmock;

import junit.framework.AssertionFailedError;
import org.jmock.MockObjectTestCase;
import org.jmock.Mock;


public class InvokedExactCountAcceptanceTest extends MockObjectTestCase {
    private final Mock mock = mock(MockedType.class,"mock");
    private final MockedType proxy = (MockedType)mock.proxy();

    interface MockedType {
        void m();
    }

    protected void setUp() throws Exception {
        mock.expects(exactly(2)).method("m").withNoArguments();
    }
    
    /*
     * Call reset() in the catch blocks for these two methods, 
     * otherwise the MockObjectTestCase infrastructure picks up
     * the errors and rethrows the exception.
     */
    public void testFailsWhenCalledFewerThanTheExactNumberOfTimes() {
        proxy.m();
        try {
            mock.verify();
        }
        catch( AssertionFailedError err ) {
            mock.reset();
            return;
        }
        fail("Should have failed");
    }
    
    
    public void testFailsWhenCalledMoreThanTheExactNumberOfTimes() {
        proxy.m();
        proxy.m();
        try {
            proxy.m();
        }
        catch( AssertionFailedError err ) {
            mock.reset();
            return;
        }
        fail("Should have failed");
    }

    public void testPassesWhenCalledTheExactNumberOfTimes() {
        proxy.m();
        proxy.m();
        mock.verify();
    }
}
