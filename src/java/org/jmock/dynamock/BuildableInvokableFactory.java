package org.jmock.dynamock;

import org.jmock.Constraint;
import org.jmock.dynamic.framework.BuildableInvokable;
import org.jmock.dynamic.framework.InvocationMatcher;
import org.jmock.dynamic.framework.Invokable;
import org.jmock.dynamic.framework.Stub;


public interface BuildableInvokableFactory {
	BuildableInvokable createBuildableInvokable();
	
	InvocationMatcher createMethodNameMatcher( String name );
	InvocationMatcher createArgumentsMatcher( Constraint[] constraints );
	
	Stub createVoidStub();
	Stub createReturnStub( Object result );
	Stub createThrowStub( Throwable throwable );
	
	/** @deprecated */
	Invokable createReturnStub(String methodName, InvocationMatcher arguments, Object result);
	/** @deprecated */
	Invokable createReturnExpectation(String methodName, InvocationMatcher arguments, Object result);
	/** @deprecated */
	Invokable createThrowableStub(String methodName, InvocationMatcher arguments, Throwable throwable);
	/** @deprecated */
	Invokable createThrowableExpectation(String methodName, InvocationMatcher arguments, Throwable throwable);
	/** @deprecated */
	Invokable createVoidStub(String methodName, InvocationMatcher arguments);
	/** @deprecated */
	Invokable createVoidExpectation(String methodName, InvocationMatcher arguments);
}
