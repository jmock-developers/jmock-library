/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.jmock.core.*;


public class CGLIBCoreMock
        extends AbstractDynamicMock
        implements MethodInterceptor
{
    public CGLIBCoreMock( Class mockedType, String name, InvocationDispatcher invocationDispatcher ) {
        super(mockedType, name, invocationDispatcher);
        setupProxyWithoutConstructorClash(Enhancer.create(mockedType, this));
    }

    public Object intercept( Object thisProxy, Method method, Object[] args, MethodProxy superProxy )
            throws Throwable 
    {
        return mockInvocation(new Invocation(proxy(), method, args));
    }
}
