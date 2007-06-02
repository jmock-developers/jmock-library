/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.cglib;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.jmock.core.AbstractDynamicMock;
import org.jmock.core.Invocation;
import org.jmock.core.InvocationDispatcher;
import org.jmock.core.LIFOInvocationDispatcher;


public class CGLIBCoreMock
        extends AbstractDynamicMock
        implements MethodInterceptor
{
    private Object proxy;

    public CGLIBCoreMock( Class mockedType ) {
        this(mockedType,
             mockNameFromClass(mockedType),
             new LIFOInvocationDispatcher());
    }

    public CGLIBCoreMock( Class mockedType, String name ) {
        this(mockedType, name, new LIFOInvocationDispatcher());
    }

    public CGLIBCoreMock( Class mockedType,
                          String name,
                          InvocationDispatcher invocationDispatcher ) {
        super(mockedType, name, invocationDispatcher);
        this.proxy = Enhancer.create(mockedType, this);
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
