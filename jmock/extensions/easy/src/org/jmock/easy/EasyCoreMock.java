/*  Copyright (c) 2004 jMock.org
 */
package org.jmock.easy;

import java.lang.reflect.Method;

import org.jmock.core.CoreMock;
import org.jmock.easy.internal.InvocationMatch;
import org.jmock.easy.internal.Range;


public class EasyCoreMock extends CoreMock
{
	private boolean isRecording = true;
	private InvocationMatch match = new InvocationMatch();
	
	public EasyCoreMock(Class mockedType) {
		super(mockedType, CoreMock.mockNameFromClass(mockedType));
	}

	public void replay() {
		addInvocationMockerAndFlush();
		isRecording = false;	
	}

	public Object invoke(Object invokedProxy, Method method, Object[] args) throws Throwable
	{
		if (isRecording) {
			addInvocationMockerAndFlush();
			match.setFromInvocation(this, method, args);
			return null;
		} 
		return super.invoke(invokedProxy, method, args);
	}

	public void setVoidCallable(Range range) {
		match.setCallCount(range);
	}

	private void addInvocationMockerAndFlush() {
		match.addInvocationMockerTo(this);
		match.flush();
	}
}
