/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.core.stub;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.core.Invocation;
import org.jmock.core.stub.TestFailureStub;
import test.jmock.core.testsupport.MethodFactory;

public class TestFailureStubTest 
	extends TestCase 
{
	static final String MESSAGE = "MESSAGE";
	
	Invocation invocation;
	TestFailureStub testFailureStub; 
	
	public void setUp() {
		MethodFactory methodFactory = new MethodFactory();
		invocation = new Invocation(
            "INVOKED-OBJECT", methodFactory.newMethodReturning(void.class), new Object[0] );
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
		
		testFailureStub.describeTo(buffer);
		
		String description = buffer.toString();
		
		assertTrue( "contains error message in description",
				    description.indexOf(MESSAGE) >= 0 );
	}
}
