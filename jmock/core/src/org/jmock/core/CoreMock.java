/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class CoreMock
    extends AbstractDynamicMock
    implements InvocationHandler
{
    public CoreMock( Class mockedType,
                     String name,
                     InvocationDispatcher invocationDispatcher )
    {
        super(mockedType, name, invocationDispatcher);
        setupProxyWithoutConstructorClash(
              Proxy.newProxyInstance(mockedType.getClassLoader(), new Class[]{mockedType}, this));
    }

    public Object invoke( Object invokedProxy, Method method, Object[] args )
        throws Throwable
    {
        return mockInvocation(new Invocation(invokedProxy, method, args));
    }
}
