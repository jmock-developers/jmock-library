/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import junit.framework.AssertionFailedError;
import org.jmock.C;
import org.jmock.stub.ReturnStub;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CoreMock implements DynamicMock {
    private InvocationDispatcher invocationDispatcher;
    private Object proxy;
    private String name;

    public CoreMock(Class mockedClass, String name, InvocationDispatcher invocationDispatcher) {
        this.proxy = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{mockedClass}, this);
        this.name = name;
        this.invocationDispatcher = invocationDispatcher;
        add(new InvocationMocker("toString", C.args(), new ReturnStub(name)));
    }

    public Object proxy() {
        return this.proxy;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Invocation invocation = new Invocation(method, args);
        try {
            if (invocation.isCheckingEqualityOnProxy()) {
                return new Boolean(args[0] == this.proxy);
            } else if (invocation.isMockNameGetter()) {
                return this.getMockName();
            } else {
                return invocationDispatcher.dispatch(invocation);
            }
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

    public void add(Invokable invokable) {
        invocationDispatcher.add(invokable);
    }

    public void reset() {
        invocationDispatcher.clear();
    }

    public static String mockNameFromClass(Class c) {
        return "mock" + className(c);
    }

    public static String className(Class c) {
        String name = c.getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
