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
    private Object proxy;

    public CGLIBCoreMock( Class mockedType ) {
        this(mockedType,
             mockNameFromClass(mockedType),
             new OrderedInvocationDispatcher(new OrderedInvocationDispatcher.LIFOInvokablesCollection()));
    }

    public CGLIBCoreMock( Class mockedType, String name ) {
        this(mockedType, name, new OrderedInvocationDispatcher(new OrderedInvocationDispatcher.LIFOInvokablesCollection()));
    }

    public CGLIBCoreMock( Class mockedType,
                          String name,
                          InvocationDispatcher invocationDispatcher ) {
        super(mockedType, name, invocationDispatcher);
        this.proxy = Enhancer.create(mockedType, this);
        invocationDispatcher.setupDefaultBehaviour(name, proxy);
    }

    public Object proxy() {
        return this.proxy;
    }

    public Object intercept( Object thisProxy, Method method, Object[] args,
                             MethodProxy superProxy )
            throws Throwable {
        Invocation invocation = new Invocation(proxy, method, args);
        return mockInvocation(invocation);
    }
}
