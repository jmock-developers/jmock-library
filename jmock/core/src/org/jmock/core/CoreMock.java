/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class CoreMock 
    extends AbstractDynamicMock
	implements InvocationHandler
{
    private Object proxy;
    
    public CoreMock( Class mockedType, String name ) {
        this( mockedType, name, new LIFOInvocationDispatcher() );
    }
    
    public CoreMock( Class mockedType, 
                     String name, 
                     InvocationDispatcher invocationDispatcher ) 
    {
        super( mockedType, name, invocationDispatcher );
        this.proxy = Proxy.newProxyInstance( mockedType.getClassLoader(), 
                                             new Class[]{mockedType}, 
                                             this);
    }
    
    public Object proxy() {
        return this.proxy;
    }
    
    public Object invoke(Object invokedProxy, Method method, Object[] args)
            throws Throwable 
    {
        Invocation invocation = new Invocation(invokedProxy,method, args);
        return mockInvocation(invocation);
    }
}
