package test.jmock.dynamic.stub;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.stub.CustomStub;

import junit.framework.TestCase;



public class CustomStubTest extends TestCase {
    static class ConcreteSideEffect extends CustomStub {
    	public ConcreteSideEffect(String description) {
    		super(description);      
        }
        
        public Object invoke( Invocation invocation) throws Throwable {
            return null;
        }
    }
    
	public void testWritesDescriptionToStringBuffer() {
        String description = "DESCRIPTION";
		CustomStub sideEffect = new ConcreteSideEffect(description);
        
        StringBuffer buf = new StringBuffer();
        
        assertSame( "should return buffer", buf, sideEffect.describeTo(buf) );
        
        assertEquals( "should be description", description, buf.toString() );
    }

}
