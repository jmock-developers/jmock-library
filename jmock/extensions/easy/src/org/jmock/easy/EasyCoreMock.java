/*  Copyright (c) 2004 jMock.org
 */
package org.jmock.easy;

import java.lang.reflect.Method;

import org.jmock.core.CoreMock;
import org.jmock.core.InvocationDispatcher;
import org.jmock.core.Invokable;
import org.jmock.core.Stub;
import org.jmock.easy.internal.InvocationMatch;
import org.jmock.easy.internal.Range;


public class EasyCoreMock extends CoreMock
{
	private boolean isRecording = true;
	private InvocationMatch match = new InvocationMatch();
    private InvocationDispatcher dispatcher;
	
	public EasyCoreMock(Class mockedType, InvocationDispatcher dispatcher) {
		super(mockedType, CoreMock.mockNameFromClass(mockedType), dispatcher);
        this.dispatcher = dispatcher;
	}

    public void addDefaultInvokable(Invokable invokable) {
        dispatcher.add(invokable);
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

    public void setExpectedStub(Range range, Stub stub) {
        match.expectCallCount(range, stub);
    }

    public void setDefaultStub(Stub stub) {
        match.addInvocationMockerTo(dispatcher);
        match.setDefault(stub);
    }

}

