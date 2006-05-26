/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.cglib;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import junit.framework.AssertionFailedError;
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
    private Object proxy = null;

    public CGLIBCoreMock(Class mockedType, String name) {
        this(mockedType, name, new LIFOInvocationDispatcher());
    }
    
    public CGLIBCoreMock(Class mockedType, String name,
            			 Class[] constructorArgumentTypes, Object[] constructorArguments)
    {
        this(mockedType, name, constructorArgumentTypes, constructorArguments,
             new LIFOInvocationDispatcher());
    }
    
    public CGLIBCoreMock(Class mockedType, String name, InvocationDispatcher invocationDispatcher) {
        this(mockedType, name, new Class[0], new Object[0], invocationDispatcher);
    }
    
    public CGLIBCoreMock(Class mockedType, String name,
                         Class[] constructorArgumentTypes, Object[] constructorArguments,
                         InvocationDispatcher invocationDispatcher) 
    {
        super(mockedType, name, invocationDispatcher);
        
        checkIsNotNonStaticInnerClass(mockedType);
        
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(mockedType.getClassLoader());
        enhancer.setSuperclass(mockedType);
        enhancer.setCallback(this);
        this.proxy = enhancer.create(constructorArgumentTypes, constructorArguments);
    }

    private void checkIsNotNonStaticInnerClass(Class mockedType) {
        if (mockedType.getDeclaringClass() != null && !Modifier.isStatic(mockedType.getModifiers())) {
            throw new AssertionFailedError("cannot mock non-static inner class " + mockedType.getName());
        }
    }
    
    public Object proxy() {
        return this.proxy;
    }

    public Object intercept(Object thisProxy, Method method, Object[] args, MethodProxy superProxy) 
    	throws Throwable 
    {
        return proxy == null
            ? superProxy.invokeSuper(thisProxy, args)
            : mockInvocation(new Invocation(proxy, method, args));
    }
}