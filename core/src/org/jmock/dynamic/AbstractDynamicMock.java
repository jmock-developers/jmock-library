package org.jmock.dynamic;

import org.jmock.Constraint;
import org.jmock.constraint.IsAnything;
import org.jmock.dynamic.matcher.ArgumentsMatcher;
import org.jmock.dynamic.matcher.NoArgumentsMatcher;
import org.jmock.dynamic.stub.CustomStub;
import org.jmock.dynamic.stub.ReturnStub;

import junit.framework.AssertionFailedError;


public abstract class AbstractDynamicMock 
    implements DynamicMock 
{
    private InvocationDispatcher invocationDispatcher;
    private Class mockedType;
    private String name;
    
    public AbstractDynamicMock( Class mockedType, String name ) {
        this( mockedType, name, new LIFOInvocationDispatcher() );
    }
    
    public AbstractDynamicMock( Class mockedType, 
                                String name, 
                                InvocationDispatcher invocationDispatcher )
    {
        this.mockedType = mockedType;
        this.name = name;
        this.invocationDispatcher = invocationDispatcher;
        
        setupDefaultBehaviour();
    }
    
    public Class getMockedType() {
        return mockedType;
    }
    
    protected Object mockInvocation( Invocation invocation ) 
        throws Throwable 
    {
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
            return new Boolean(invocation.getParameterValues().get(0) == proxy());
        }
    }
    
    private class HashCodeStub extends CustomStub {
        private HashCodeStub() {
            super("returns hashCode for proxy");
        }
        public Object invoke( Invocation invocation ) throws Throwable {
            return new Integer(AbstractDynamicMock.this.hashCode());
        }
    }
}
