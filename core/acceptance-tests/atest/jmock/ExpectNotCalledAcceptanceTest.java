package atest.jmock;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.DynamicMockError;


public class ExpectNotCalledAcceptanceTest 
    extends MockObjectTestCase 
{
    private static interface MockedInterface {
        public void method();
    }
    
    public void testExpectNotCalledOverridesStubAndFailsIfCalled() {
        Mock mock = new Mock(MockedInterface.class,"mock");
        
        mock.stub().method("method").noParams();
        mock.expect(notCalled()).method("method").noParams();
        
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
        
        mock.stub().method("method").noParams().isVoid();
        mock.expect(notCalled()).method("method").noParams();
        
        mock.verify();
    }
}
