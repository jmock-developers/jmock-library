/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.framework;

import junit.framework.AssertionFailedError;
import org.jmock.dynamic.matcher.MethodNameMatcher;
import org.jmock.dynamic.stub.VoidStub;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InvocationMocker implements Invokable {

    private List matchers = new ArrayList();
    private Stub stub;

    public InvocationMocker(String methodName, InvocationMatcher arguments, Stub stub) {
        this(stub);
        addMatcher(new MethodNameMatcher(methodName));
        addMatcher(arguments);
    }

    public InvocationMocker(InvocationMatcher[] matchers, Stub stub) {
        this(stub);
        for (int i = 0; i < matchers.length; i++) addMatcher(matchers[i]);
    }

    public InvocationMocker(Stub stub) {
        this.stub = stub;
    }

    public InvocationMocker() {
    	this( VoidStub.INSTANCE );
    }
    
    public boolean matches(Invocation invocation) {
        Iterator i = matchers.iterator();
        while (i.hasNext()) {
            if (!((InvocationMatcher) i.next()).matches(invocation)) {
                return false;
            }
        }
        return true;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        Iterator i = matchers.iterator();
        while (i.hasNext()) {
            ((InvocationMatcher) i.next()).invoked(invocation);
        }
        return stub.invoke(invocation);
    }

    public void verify() {
        try {
            Iterator i = matchers.iterator();
            while (i.hasNext()) {
                ((InvocationMatcher) i.next()).verify();
            }
        } catch (AssertionFailedError error) {
            AssertionFailedError newError = new AssertionFailedError(error.getMessage() + " " +
                    toString());
            newError.fillInStackTrace();
            throw newError;
        }
    }

    public InvocationMocker addMatcher(InvocationMatcher matcher) {
        matchers.add(matcher);
        return this;
    }

    public void setStub(Stub stub) {
        this.stub = stub;
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        buffer.append("(");
        Iterator it = matchers.iterator();
        while (it.hasNext()) {
            ((InvocationMatcher) it.next()).writeTo(buffer).append(", ");
        }
        stub.writeTo(buffer);
        return buffer.append("\n");
    }

    public String toString() {
        return writeTo(new StringBuffer()).toString();
    }

}
