/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import junit.framework.AssertionFailedError;

import org.jmock.Constraint;
import org.jmock.constraint.IsAnything;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.NoArgumentsMatcher;
import org.jmock.dynamic.stub.CustomStub;
import org.jmock.dynamic.stub.ReturnStub;


public class CoreMock 
	implements DynamicMock 
{
    private InvocationDispatcher invocationDispatcher;
    private Object proxy;
    private String name;

    public CoreMock(Class mockedClass, String name, InvocationDispatcher invocationDispatcher ) {
        this.proxy = Proxy.newProxyInstance( getClass().getClassLoader(), 
                                             new Class[]{mockedClass}, 
                                             this);
        this.name = name;
        this.invocationDispatcher = invocationDispatcher;
        
        setupDefaultBehaviour();
    }
    
    public Object proxy() {
        return this.proxy;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable 
    {
        Invocation invocation = new Invocation(method, args);
        try {
            return invocationDispatcher.dispatch(invocation);
        } catch (DynamicMockError error) {
            DynamicMockError newError = new DynamicMockError(invocation, invocationDispatcher, name);
            newError.fillInStackTrace();
            throw newError;
        } catch (AssertionFailedError ex) {
            DynamicMockError error = new DynamicMockError(invocation, invocationDispatcher, ex.getMessage());
            error.fillInStackTrace();
            throw error;
        }
    }
    
    public void verify() {
        try {
            invocationDispatcher.verify();
        } catch (AssertionFailedError ex) {
            throw new AssertionFailedError(name + ": " + ex.getMessage());
        }
    }
    
    public String toString() {
        return this.name;
    }
    
    public String getMockName() {
        return this.name;
    }
    
	public void setDefaultStub(Stub newDefaultStub) {
		invocationDispatcher.setDefaultStub(newDefaultStub);
	}
	
    public void add(Invokable invokable) {
        invocationDispatcher.add(invokable);
    }
    
    public void reset() {
        //TODO write tests for this
        invocationDispatcher.clear();
        setupDefaultBehaviour();
    }
    
    public static String mockNameFromClass(Class c) {
        return "mock" + DynamicUtil.classShortName(c);
    }
    
    private void setupDefaultBehaviour() {
        add(new InvocationMocker("toString", NoArgumentsMatcher.INSTANCE, new ReturnStub(this.name)));
        add(new InvocationMocker("equals", 
                new ArgumentsMatcher(new Constraint[] {new IsAnything()}), 
                new IsSameAsProxy(this.proxy)));
    }

    private static class IsSameAsProxy extends CustomStub {
        private Object proxy;
        
        private IsSameAsProxy(Object proxy) {
            super("returns whether equal to proxy");
            this.proxy = proxy;
        }
        public Object invoke( Invocation invocation ) throws Throwable {
            return new Boolean(invocation.getParameterValues().get(0) == proxy);
        }
    }
}
