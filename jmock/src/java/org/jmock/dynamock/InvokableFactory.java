/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamock;

import org.jmock.Constraint;
import org.jmock.dynamic.framework.BuildableInvokable;
import org.jmock.dynamic.framework.InvocationMatcher;
import org.jmock.dynamic.framework.InvocationMocker;
import org.jmock.dynamic.framework.Invokable;
import org.jmock.dynamic.framework.Stub;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.CallOnceMatcher;
import org.jmock.dynamic.matcher.MethodNameMatcher;
import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.ThrowStub;
import org.jmock.dynamic.stub.VoidStub;


public class InvokableFactory 
	implements BuildableInvokableFactory 
{
	public InvocationMatcher createArgumentsMatcher(Constraint[] constraints) {
		return new ArgumentsMatcher(constraints);
	}
	
	public BuildableInvokable createBuildableInvokable() {
		return new InvocationMocker();
	}
	
	public InvocationMatcher createMethodNameMatcher(String name) {
		return new MethodNameMatcher(name);
	}

	public Stub createReturnStub(Object result) {
		return new ReturnStub(result);
	}
	
	public Stub createThrowStub(Throwable throwable) {
		return new ThrowStub(throwable);
	}

	public Stub createVoidStub() {
		return VoidStub.INSTANCE;
	}

    public Invokable createReturnStub(String methodName, InvocationMatcher arguments, Object result) {
        return new InvocationMocker(methodName, arguments, new ReturnStub(result));
    }

    public Invokable createReturnExpectation(String methodName, InvocationMatcher arguments, Object result) {
        return callOnce(new InvocationMocker(methodName, arguments, new ReturnStub(result)));
    }

    public Invokable createThrowableStub(String methodName, InvocationMatcher arguments, Throwable throwable) {
        return new InvocationMocker(methodName, arguments, new ThrowStub(throwable));
    }

    public Invokable createThrowableExpectation(String methodName, InvocationMatcher arguments, Throwable throwable) {
        return callOnce(new InvocationMocker(methodName, arguments, new ThrowStub(throwable)));
    }

    public Invokable createVoidStub(String methodName, InvocationMatcher arguments) {
        return new InvocationMocker(methodName, arguments, new VoidStub());
    }

    public Invokable createVoidExpectation(String methodName, InvocationMatcher arguments) {
        return callOnce(new InvocationMocker(methodName, arguments, new VoidStub()));
    }

    private Invokable callOnce(InvocationMocker mocker) {
        mocker.addMatcher(new CallOnceMatcher());
        return mocker;
    }
}
