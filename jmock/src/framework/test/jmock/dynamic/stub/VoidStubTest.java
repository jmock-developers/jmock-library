/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.stub;

import junit.framework.TestCase;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.stub.VoidStub;

public class VoidStubTest 
	extends TestCase 
{
	Invocation invocation;
	VoidStub voidStub; 
	
	public void setUp() {
		invocation = new Invocation( Void.class, "ignoredName", new Class[0], 
				void.class, new Object[0]);
		voidStub  = new VoidStub();
	}
	
	public void testReturnsNullWhenInvoked() throws Throwable {
        assertNull( "Should return null", 
        		    new VoidStub().invoke(invocation) );
    }

    public void testIncludesVoidInDescription() {
    	StringBuffer buffer = new StringBuffer();
    	
    	voidStub.writeTo(buffer);
    	
    	String description = buffer.toString();
    	
    	assertTrue( "contains 'void' in description",
    				description.indexOf("void") >= 0 );
    }
}
