/*  Copyright (c) 2004 jMock.org
 */
package org.jmock.easy;

import org.jmock.core.CoreMock;
import org.jmock.core.FIFOInvocationDispatcher;
import org.jmock.core.Stub;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.VoidStub;
import org.jmock.core.stub.ThrowStub;
import org.jmock.easy.internal.InvocationMatch;
import org.jmock.easy.internal.Range;

import java.lang.reflect.Method;


public class EasyCoreMock extends CoreMock
{
	private boolean isRecording = true;
	private InvocationMatch match = new InvocationMatch();
	
	public EasyCoreMock(Class mockedType) {
		super(mockedType, CoreMock.mockNameFromClass(mockedType), new FIFOInvocationDispatcher());
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
		match.addInvocationMockerTo(this);
		match.flush();
	}

    public void setExpectedStub(Range range, Stub stub) {
        match.expectCallCount(range);
        match.setStub(stub);
    }

    public void setDefaultStub(Stub stub) {
        match.addInvocationMockerTo(this);
        match.stub();
        match.setStub(stub);
    }
}

