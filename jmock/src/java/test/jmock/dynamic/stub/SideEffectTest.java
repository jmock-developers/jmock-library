package test.jmock.dynamic.stub;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.stub.SideEffect;

import junit.framework.TestCase;



public class SideEffectTest extends TestCase {
    static class ConcreteSideEffect extends SideEffect {
    	public ConcreteSideEffect(String description) {
    		super(description);      
        }
        
        public Object invoke( Invocation invocation) throws Throwable {
            return null;
        }
    }
    
	public void testWritesDescriptionToStringBuffer() {
        String description = "DESCRIPTION";
		SideEffect sideEffect = new ConcreteSideEffect(description);
        
        StringBuffer buf = new StringBuffer();
        
        assertSame( "should return buffer", buf, sideEffect.writeTo(buf) );
        
        assertEquals( "should be description", description, buf.toString() );
    }

}
