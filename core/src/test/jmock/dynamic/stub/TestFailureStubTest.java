/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.stub;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.stub.TestFailureStub;

public class TestFailureStubTest 
	extends TestCase 
{
	static final String MESSAGE = "MESSAGE";
	
	Invocation invocation;
	TestFailureStub testFailureStub; 
	
	public void setUp() {
		invocation = new Invocation( 
            "INVOKED-OBJECT", Void.class, "ignoredName",  new Class[0], 
			void.class, new Object[0]);
		testFailureStub  = new TestFailureStub(MESSAGE);
	}
	
	public void testThrowsAssertionFailedErrorWhenInvoked() throws Throwable {
		try {
			testFailureStub.invoke(invocation);
		} catch (AssertionFailedError ex ) {
            assertEquals( "should be error message from stub",
                          MESSAGE, ex.getMessage() );
            return;
		}
        fail("expected AssertionFailedError");
	}
	
	public void testIncludesErrorMessageInDescription() {
		StringBuffer buffer = new StringBuffer();
		
		testFailureStub.writeTo(buffer);
		
		String description = buffer.toString();
		
		assertTrue( "contains error message in description",
				    description.indexOf(MESSAGE) >= 0 );
	}
}
