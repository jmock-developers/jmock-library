package test.jmock.builder;

import junit.framework.TestCase;

import org.jmock.builder.Mock;
import org.jmock.dynamic.DynamicMockError;


public class Mock_OrderedInvocations_IntegrationTest 
    extends TestCase
{
	private Mock mock;
	private ExampleInterface proxy;
	
	public static interface ExampleInterface {
		void hello();
        void goodbye();
    }
    
    public void setUp() {
        mock = new Mock(ExampleInterface.class,"mock");
        proxy = (ExampleInterface)mock.proxy();
    }
    
    public void testOrderedCallsCanOccurInOrder() {
    	mock.method("hello").isVoid().id("hello call");
    	mock.method("goodbye").after("hello call");
    	
    	proxy.hello();
        proxy.goodbye();
        
        mock.verify();
    }
    
	public void testOrderedCallsMustNotOccurOutOfOrder() {
		mock.method("hello").isVoid().id("hello call");
		mock.method("goodbye").after("hello call");
		
		try {
            proxy.goodbye();
            proxy.hello();
            fail("should have thrown DynamicMockError");
        }
        catch( DynamicMockError ex ) {
            // expected
        }
        
        mock.verify();
    }
	
	public void testOrderingConstraintsDoNotImplyExpectedCall() {
		mock.method("hello").isVoid().id("hello call");
		mock.method("goodbye").after("hello call");
		
		mock.verify();
	}
}
