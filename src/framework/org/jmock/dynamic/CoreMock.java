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
    private Class[] mockedTypes;
    private Object proxy;
    private String name;

    public CoreMock( Class mockedType, 
                     String name, 
                     InvocationDispatcher invocationDispatcher ) 
    {
        this.mockedTypes = new Class[]{mockedType};
        this.proxy = Proxy.newProxyInstance( getClass().getClassLoader(), 
                                             mockedTypes, 
                                             this);
        this.name = name;
        this.invocationDispatcher = invocationDispatcher;
        
        setupDefaultBehaviour();
    }
    
    public Class[] getMockedTypes() {
        return (Class[])mockedTypes.clone();
    }
    
    public Object proxy() {
        return this.proxy;
    }
    
    public Object invoke(Object invokedProxy, Method method, Object[] args)
            throws Throwable 
    {
        Invocation invocation = new Invocation(method, args);
        try {
            return invocationDispatcher.dispatch(invocation);
        }
        catch (AssertionFailedError failure) {
            DynamicMockError mockFailure = 
            	new DynamicMockError(this, invocation, invocationDispatcher, failure.getMessage());
            
			mockFailure.fillInStackTrace();
			throw mockFailure;
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
        add( new SilentInvocationMocker( "toString", 
            NoArgumentsMatcher.INSTANCE, 
            new ReturnStub(name)));
        add( new SilentInvocationMocker("equals", 
            new ArgumentsMatcher(new Constraint[] {new IsAnything()}), 
            new IsSameAsProxyStub()));
        add( new SilentInvocationMocker("hashCode",
            NoArgumentsMatcher.INSTANCE,
            new HashCodeStub()));
    }
    
    private static class SilentInvocationMocker extends InvocationMocker {
        public SilentInvocationMocker(String methodName, InvocationMatcher arguments, Stub stub) {
            super(methodName, arguments, stub);
        }

        public StringBuffer writeTo(StringBuffer buffer) {
            return buffer;
        }
    }
    
    private class IsSameAsProxyStub extends CustomStub {
        private IsSameAsProxyStub() {
            super("returns whether equal to proxy");
        }
        
        public Object invoke( Invocation invocation ) throws Throwable {
            return new Boolean(invocation.getParameterValues().get(0) == proxy);
        }
    }
    
    private class HashCodeStub extends CustomStub {
        private HashCodeStub() {
            super("returns hashCode for proxy");
        }
        public Object invoke( Invocation invocation ) throws Throwable {
            return new Integer(CoreMock.this.hashCode());
        }
    }
}
