/*  Copyright (c) 2004 jMock.org
 */
package org.jmock.easy;

import java.lang.reflect.Method;

import org.jmock.core.CoreMock;
import org.jmock.core.InvocationDispatcher;
import org.jmock.core.Stub;
import org.jmock.easy.internal.InvocationMatch;


public class EasyCoreMock extends CoreMock
{
	private boolean isRecording = true;
	private InvocationMatch match;
    private InvocationDispatcher dispatcher;
	
	public EasyCoreMock(Class mockedType, InvocationDispatcher dispatcher, InvocationMatch match) {
		super(mockedType, CoreMock.mockNameFromClass(mockedType), dispatcher);
        this.match = match;
        this.dispatcher = dispatcher;
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

	private void addInvocationMockerAndFlush() {
		match.addInvocationMockerTo(dispatcher);
		match.flush();
	}

    public void setDefaultStub(Stub stub) {
        match.addInvocationMockerTo(dispatcher);
        match.setDefault(stub);
    }

}

