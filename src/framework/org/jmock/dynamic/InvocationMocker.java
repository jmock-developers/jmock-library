/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import junit.framework.AssertionFailedError;
import org.jmock.dynamic.matcher.MethodNameMatcher;
import org.jmock.dynamic.stub.VoidStub;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class InvocationMocker 
	implements BuildableInvokable
{
    private String name;
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

    public void setName(String name) {
        this.name = name;
    }

    public void addMatcher(InvocationMatcher matcher) {
        matchers.add(matcher);
    }

    public void setStub(Stub stub) {
        this.stub = stub;
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        Iterator it = matchers.iterator();
        while (it.hasNext()) {
            int oldLength = buffer.length();
            ((InvocationMatcher) it.next()).writeTo(buffer);
            
            if( buffer.length() != oldLength ) buffer.append(", ");
        }
        stub.writeTo(buffer);
        
        if( name != null ) {
            buffer.append( " [").append(name).append("]");
        }
        return buffer.append("\n");
    }
    
    public String toString() {
        return writeTo(new StringBuffer()).toString();
    }
}
