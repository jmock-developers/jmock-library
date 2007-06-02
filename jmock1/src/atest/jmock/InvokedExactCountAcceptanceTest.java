package atest.jmock;

import junit.framework.AssertionFailedError;
import org.jmock.MockObjectTestCase;
import org.jmock.Mock;


public class InvokedExactCountAcceptanceTest extends MockObjectTestCase {
    private static final int EXPECTATION_COUNT = 4;
	
    private final Mock mock = mock(MockedType.class,"mock");
    private final MockedType proxy = (MockedType)mock.proxy();

    interface MockedType {
        void m();
    }

    protected void setUp() throws Exception {
        mock.expects(exactly(EXPECTATION_COUNT)).method("m").withNoArguments();
    }
    
    /*
     * Call reset() in the catch blocks for these two methods, 
     * otherwise the MockObjectTestCase infrastructure picks up
     * the errors and rethrows the exception.
     */
    public void testFailsWhenCalledFewerThanTheExactNumberOfTimes() {
		invokeRepeatedly(EXPECTATION_COUNT-1);
    	
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
    	invokeRepeatedly(EXPECTATION_COUNT);
    	
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
        invokeRepeatedly(EXPECTATION_COUNT);
        mock.verify();
    }

	private void invokeRepeatedly(int invocationCount) {
		for (int i = 0; i < invocationCount; i++) {
    		proxy.m();
    	}
	}
}
