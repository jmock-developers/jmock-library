/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.stub;

import junit.framework.TestCase;
import test.jmock.dynamic.DummyThrowable;
import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.stub.ThrowStub;

public class ThrowStubTest 
	extends TestCase 
{
	static final Throwable THROWABLE = new DummyThrowable();
	
	Invocation invocation;
	ThrowStub throwStub; 
	
	public void setUp() {
		invocation = new Invocation( Void.class, "ignoredName", 
				new Class[0], void.class, new Object[0]);
		throwStub  = new ThrowStub(THROWABLE);
	}
	
	public void testThrowsThrowableObjectPassedToConstructorWhenInvoked() {
		try {
			throwStub.invoke(invocation);
		} catch (Throwable t) {
			assertSame("Should be the same throwable", THROWABLE, t);
		}
	}
	
	public void testIncludesDetailsOfThrowableInDescription() {
		StringBuffer buffer = new StringBuffer();
		
		throwStub.writeTo(buffer);
		
		String description = buffer.toString();
		
		assertTrue( "contains class of thrown object in description",
				    description.indexOf(THROWABLE.toString()) >= 0 );
		assertTrue( "contains 'throws' in description",
					description.indexOf("throws") >= 0 );
	}
}
