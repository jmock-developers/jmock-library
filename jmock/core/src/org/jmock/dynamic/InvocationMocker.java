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
    public interface Describer {
        public boolean hasDescription();
        
        public void describeTo( StringBuffer buf,
                                List matchers, Stub stub, String name );
    }
    
    
    private String name = null; // default to no name
    private List matchers = new ArrayList();
    private Stub stub = VoidStub.INSTANCE;
    
    
    public InvocationMocker() {
    }
    
    public InvocationMocker(String methodName, InvocationMatcher arguments, Stub stub) {
        addMatcher(new MethodNameMatcher(methodName));
        addMatcher(arguments);
        setStub(stub);
    }

    public InvocationMocker(InvocationMatcher[] matchers, Stub stub) {
        for (int i = 0; i < matchers.length; i++) addMatcher(matchers[i]);
        setStub(stub);
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
    
    public boolean hasDescription() {
        return true;
    }
    
    //TODO: make the description configurable by delegating to a formatter interface
    public StringBuffer describeTo(StringBuffer buffer) {
        Iterator it = matchers.iterator();
        while (it.hasNext()) {
            InvocationMatcher matcher = (InvocationMatcher) it.next();
            
            if( matcher.hasDescription() ) {
                matcher.describeTo(buffer).append(", ");
            }
        }
        
        stub.describeTo(buffer);
        
        if( name != null ) {
            buffer.append( " [").append(name).append("]");
        }
        
        return buffer;
    }
    
    public String toString() {
        return describeTo(new StringBuffer()).toString();
    }
}
