package atest.jmock.dynamock;

import org.jmock.dynamock.Mock;
import org.jmock.dynamock.MockObjectTestCase;
import org.jmock.dynamic.DynamicMockError;


public class ExpectNotCalledAcceptanceTest 
    extends MockObjectTestCase 
{
    private static interface MockedInterface {
        public void method();
    }
    
    public void testExpectNotCalledOverridesStubAndFailsIfCalled() {
        Mock mock = new Mock(MockedInterface.class,"mock");
        
        mock.stubVoid("method", NO_ARGS );
        mock.expectNotCalled("method", NO_ARGS );
        
        try {
            ((MockedInterface)mock.proxy()).method();
        }
        catch( DynamicMockError error ) {
            return;
        }
        fail("DynamicMockError expected");
    }
    
    public void testExpectNotCalledVerifiesIfNotCalled() {
        Mock mock = new Mock(MockedInterface.class,"mock");
        
        mock.stubVoid("method", NO_ARGS );
        mock.expectNotCalled("method", NO_ARGS );
        
        mock.verify();
    }
}
