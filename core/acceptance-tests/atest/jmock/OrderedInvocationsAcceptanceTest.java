package atest.jmock;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.Mock;
import org.jmock.core.DynamicMockError;
import org.jmock.expectation.AssertMo;


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
    	mock.stub().method("hello").id("hello call");
    	mock.stub().method("goodbye").after("hello call");
    	
    	proxy.hello();
        proxy.goodbye();
        
        mock.verify();
    }
    
	public void testOrderedCallsMustNotOccurOutOfOrder() {
		String priorCall = "HELLO-CALL-ID";
		
		mock.stub().method("hello").id(priorCall);
		mock.stub().method("goodbye").after(priorCall);
		
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
		mock.stub().method("hello").id("hello call");
		mock.stub().method("goodbye").after("hello call");
		mock.stub().method("moreTeaVicar");
		
		proxy.hello();
		proxy.moreTeaVicar();
		proxy.goodbye();
		
		mock.verify();
	}
	
	public void testOrderingConstraintsDoNotImplyExpectedCall() {
		mock.stub().method("hello").isVoid().id("hello call");
		mock.stub().method("goodbye").after("hello call");
		
		mock.verify();
	}
	
	public void testCanUseMethodNameAsDefaultInvocationID() {
		mock.stub().method("hello").isVoid();
		mock.stub().method("goodbye").after("hello");
		
		mock.verify();
	}
	
	public void testCanSpecifyOrderOverDifferentMocks() {
		Mock otherMock = new Mock( ExampleInterface.class, "otherMock" );
		ExampleInterface otherProxy = (ExampleInterface)otherMock.proxy();
		
		otherMock.stub().method("hello").isVoid();
		
		mock.stub().method("goodbye").after(otherMock,"hello");
		
		otherProxy.hello();
		proxy.goodbye();
	}
	
	public void testDetectsUnexpectedOrderOverDifferentMocks() {
		String otherMockName = "otherMock";
		String priorCall = "HELLO-CALL-ID";
		Mock otherMock = new Mock( ExampleInterface.class, otherMockName );
		
		otherMock.stub().method("hello").id(priorCall);
		mock.stub().method("goodbye").after(otherMock,priorCall);
		
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
        mock.stub().method("hello").id("hello #1");
        mock.stub().method("hello").after("hello #1").id("hello #2");
        mock.stub().method("hello").after("hello #2").id("hello #3");
        mock.stub().method("goodbye").after("hello #3");
        
        proxy.hello();
        proxy.hello();
        proxy.hello();
        proxy.goodbye();
        
        mock.verify();
    }
    
    public void testDetectsDuplicateIDs() {
        String duplicateID = "DUPLICATE-ID";
        
        mock.stub().method("hello").id(duplicateID);
        
        try {
            mock.stub().method("hello").id(duplicateID);
        }
        catch( AssertionFailedError ex ) {
            AssertMo.assertIncludes( "error message contains duplicate id",
                                     duplicateID, ex.getMessage() );
            return;
        }
        fail("should have failed");
    }
}
