package atest.jmock.builder;

import junit.framework.TestCase;

import org.jmock.builder.Mock;
import org.jmock.dynamic.DynamicMockError;


public class OrderedInvocationsAcceptanceTest 
    extends TestCase
{
	private Mock mock;
	private ExampleInterface proxy;
	
	public static interface ExampleInterface {
		void hello();
        void goodbye();
        void moreTeaVicar();
    }
    
    public void setUp() {
        mock = new Mock(ExampleInterface.class,"mock");
        proxy = (ExampleInterface)mock.proxy();
    }
    
    public void testOrderedCallsCanOccurInOrder() {
    	mock.method("hello").id("hello call");
    	mock.method("goodbye").after("hello call");
    	
    	proxy.hello();
        proxy.goodbye();
        
        mock.verify();
    }
    
	public void testOrderedCallsMustNotOccurOutOfOrder() {
		String priorCall = "HELLO-CALL-ID";
		
		mock.method("hello").id(priorCall);
		mock.method("goodbye").after(priorCall);
		
		try {
            proxy.goodbye();
            fail("should have thrown DynamicMockError");
        }
        catch( DynamicMockError ex ) {
            assertTrue( "error message should contain id of prior call",
        				ex.getMessage().indexOf(priorCall) >= 0 );
        }
        
        mock.verify();
    }
	
	public void testOrderingDoesNotAffectUnrelatedCalls() {
		mock.method("hello").id("hello call");
		mock.method("goodbye").after("hello call");
		mock.method("moreTeaVicar");
		
		proxy.hello();
		proxy.moreTeaVicar();
		proxy.goodbye();
		
		mock.verify();
	}
	
	public void testOrderingConstraintsDoNotImplyExpectedCall() {
		mock.method("hello").isVoid().id("hello call");
		mock.method("goodbye").after("hello call");
		
		mock.verify();
	}
	
	public void testCanUseMethodNameAsDefaultInvocationID() {
		mock.method("hello").isVoid();
		mock.method("goodbye").after("hello");
		
		mock.verify();
	}
	
	public void testCanSpecifyOrderOverDifferentMocks() {
		Mock otherMock = new Mock( ExampleInterface.class, "otherMock" );
		ExampleInterface otherProxy = (ExampleInterface)otherMock.proxy();
		
		otherMock.method("hello").isVoid();
		
		mock.method("goodbye").after(otherMock,"hello");
		
		otherProxy.hello();
		proxy.goodbye();
	}
	
	public void testDetectsUnexpectedOrderOverDifferentMocks() {
		String otherMockName = "otherMock";
		String priorCall = "HELLO-CALL-ID";
		Mock otherMock = new Mock( ExampleInterface.class, otherMockName );
		
		otherMock.method("hello").id(priorCall);
		mock.method("goodbye").after(otherMock,priorCall);
		
		try {
			proxy.goodbye();
			fail("expected DynamicMockError");
		}
		catch( DynamicMockError ex ) {
			assertTrue( "error message should contain id of prior call",
						ex.getMessage().indexOf(priorCall) >= 0 );
			assertTrue( "error message should contain name of mock receiving prior call",
						ex.getMessage().indexOf(otherMockName) >= 0 );
			
		}
	}
	
    public void testAllowsSameInvocationMultipleTimes() {
        mock.method("hello").id("hello #1");
        mock.method("hello").after("hello #1").id("hello #2");
        mock.method("hello").after("hello #2").id("hello #3");
        mock.method("goodbye").after("hello #3");
        
        proxy.hello();
        proxy.hello();
        proxy.hello();
        proxy.goodbye();
        
        mock.verify();
    }
}
