package org.jmock.dynamock;

import org.jmock.Constraint;
import org.jmock.dynamic.framework.BuildableInvokable;
import org.jmock.dynamic.framework.InvocationMatcher;
import org.jmock.dynamic.framework.Stub;


public interface BuildableInvokableFactory {
	BuildableInvokable createBuildableInvokable();
	
	InvocationMatcher createMethodNameMatcher( String name );
	InvocationMatcher createArgumentsMatcher( Constraint[] constraints );
	InvocationMatcher createCallOnceMatcher();
	
	Stub createVoidStub();
	Stub createReturnStub( Object result );
	Stub createThrowStub( Throwable throwable );
}
