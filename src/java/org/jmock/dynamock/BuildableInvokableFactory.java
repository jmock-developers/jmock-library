package org.jmock.dynamock;

import org.jmock.Constraint;
import org.jmock.dynamic.BuildableInvokable;
import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.Stub;


public interface BuildableInvokableFactory {
	BuildableInvokable createBuildableInvokable();
	
	InvocationMatcher createMethodNameMatcher( String name );
	InvocationMatcher createArgumentsMatcher( Constraint[] constraints );
	InvocationMatcher createCallOnceMatcher();
	
	Stub createVoidStub();
	Stub createReturnStub( Object result );
	Stub createThrowStub( Throwable throwable );
}
