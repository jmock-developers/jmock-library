/*  Copyright (c) 2004 jMock.org
 */
package org.jmock.easy;

import java.lang.reflect.Method;

import org.jmock.core.CoreMock;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;
import org.jmock.core.stub.DefaultResultStub;
import org.jmock.easy.internal.EasyInvocationDispatcher;
import org.jmock.easy.internal.InvocationMatch;


public class EasyCoreMock extends CoreMock
{
	private boolean isRecording = true;
	private InvocationMatch match;
    private EasyInvocationDispatcher dispatcher;
    private Stub defaultReturnStub = new DefaultResultStub();
	
	public EasyCoreMock(Class mockedType, EasyInvocationDispatcher dispatcher, InvocationMatch match) {
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
			match.setFromInvocation(method, args);
			return defaultReturnStub.invoke(new Invocation(invokedProxy, method, args));
		}
		return super.invoke(invokedProxy, method, args);
	}

	private void addInvocationMockerAndFlush() {
		match.addInvocationMockerTo(dispatcher);
		match.flush();
	}

    public void setDefaultStub(Stub stub) {
        match.addInvocationMockerTo(dispatcher);
        match.setDefaultForMethod(stub);
    }

}

