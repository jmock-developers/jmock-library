/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.stub;

import junit.framework.TestCase;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.stub.ReturnStub;

public class ReturnStubTest 
	extends TestCase 
{
	static final String RESULT = "result";
	
    Invocation invocation;
    ReturnStub returnStub; 
    
    public void setUp() {
	    invocation = new Invocation( Void.class, "test", "ignoredName", 
	    							 new Class[0], void.class, new Object[0]);
	    returnStub  = new ReturnStub(RESULT);
    }
    
    public void testReturnsValuePassedToConstructor() throws Throwable {
        assertSame( "Should be the same result object", 
        		    RESULT, returnStub.invoke(invocation) );
    }
    
    public void testIncludesValueInDescription() {
    	StringBuffer buffer = new StringBuffer();
    	
    	returnStub.writeTo(buffer);
    	
    	String description = buffer.toString();
    	
    	assertTrue( "contains result in description",
    				description.indexOf(RESULT.toString()) >= 0 );
    	assertTrue( "contains 'returns' in description",
    				description.indexOf("returns") >= 0 );
    }
}
