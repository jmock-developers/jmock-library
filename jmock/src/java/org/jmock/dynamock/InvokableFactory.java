/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamock;

import org.jmock.Constraint;
import org.jmock.dynamic.framework.BuildableInvokable;
import org.jmock.dynamic.framework.InvocationMatcher;
import org.jmock.dynamic.framework.InvocationMocker;
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
	
	public InvocationMatcher createCallOnceMatcher() {
		return new CallOnceMatcher();
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
}
