/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.util;

import org.jmock.dynamic.CoreMock;
import org.jmock.dynamic.DynamicUtil;
import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.InvocationMocker;
import org.jmock.dynamic.LIFOInvocationDispatcher;
import org.jmock.dynamic.matcher.StatelessInvocationMatcher;
import org.jmock.dynamic.stub.CustomStub;

public class Dummy {
	/** This class cannot be instantiated. */
	private Dummy() {
	}

	public static Object newDummy(Class interfaceClass) {
		return newDummy( interfaceClass, "dummy"+ DynamicUtil.classShortName(interfaceClass) );
	}

	public static Object newDummy( final Class interfaceClass, final String name ) {
		CoreMock mock = new CoreMock( interfaceClass, name, new LIFOInvocationDispatcher() );
        InvocationMocker mocker = new InvocationMocker();
        
        mocker.addMatcher( new StatelessInvocationMatcher() {
            public boolean matches( Invocation invocation ) {
            	return invocation.getDeclaringClass() == interfaceClass;
            }
            public StringBuffer writeTo(StringBuffer buf) {
            	return buf.append("anu method declared in " + interfaceClass);
            }
        } );
        mocker.setStub( new CustomStub("dummy method") {
        	public Object invoke( Invocation invocation ) throws Throwable {
                throw new NotImplementedException( 
                    invocation.getMethodName() + "called on " + name );
            }
        } );
        
        mock.add(mocker);
        
        return mock.proxy();
	}

	public static Object newDummy( final String name ) {
        return new Object() {
        	public String toString() {
        		return name;
            }
        };
	}
}
