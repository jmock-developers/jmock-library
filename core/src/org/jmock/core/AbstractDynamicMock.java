/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.util.List;
import junit.framework.AssertionFailedError;
import org.jmock.core.constraint.IsAnything;
import org.jmock.core.matcher.ArgumentsMatcher;
import org.jmock.core.matcher.MethodNameMatcher;
import org.jmock.core.matcher.NoArgumentsMatcher;
import org.jmock.core.stub.CustomStub;
import org.jmock.core.stub.ReturnStub;


public abstract class AbstractDynamicMock
        implements DynamicMock
{
    private InvocationDispatcher invocationDispatcher;
    private Class mockedType;
    private String name;
    private DynamicMockError firstFailure = null;


    public AbstractDynamicMock( Class mockedType, String name ) {
        this(mockedType, name, new LIFOInvocationDispatcher());
    }

    public AbstractDynamicMock( Class mockedType,
                                String name,
                                InvocationDispatcher invocationDispatcher ) {
        this.mockedType = mockedType;
        this.name = name;
        this.invocationDispatcher = invocationDispatcher;

        setupDefaultBehaviour();
    }

    public Class getMockedType() {
        return mockedType;
    }

    protected Object mockInvocation( Invocation invocation )
            throws Throwable {
        try {
            return invocationDispatcher.dispatch(invocation);
        }
        catch (AssertionFailedError failure) {
            if (firstFailure == null) {
                firstFailure = new DynamicMockError(this, invocation, invocationDispatcher, failure.getMessage());
                firstFailure.fillInStackTrace();
            }

            throw firstFailure;
        }
    }

    public void verify() {
        forgetFirstFailure();

        try {
            invocationDispatcher.verify();
        }
        catch (AssertionFailedError ex) {
            throw new AssertionFailedError( "mock object " + name + ": " + ex.getMessage());
        }
    }

    public String toString() {
        return this.name;
    }

    public String getMockName() {
        return this.name;
    }

    public void setDefaultStub( Stub newDefaultStub ) {
        invocationDispatcher.setDefaultStub(newDefaultStub);
    }

    public void addInvokable( Invokable invokable ) {
        invocationDispatcher.add(invokable);
    }

    public void reset() {
        //TODO write tests for this
        invocationDispatcher.clear();
        forgetFirstFailure();
        setupDefaultBehaviour();
    }

    public static String mockNameFromClass( Class c ) {
        return "mock" + Formatting.classShortName(c);
    }

    private void setupDefaultBehaviour() {
        addInvokable(hiddenInvocationMocker("toString",
                                            NoArgumentsMatcher.INSTANCE,
                                            new ReturnStub(name)));
        addInvokable(hiddenInvocationMocker("equals",
                                            new ArgumentsMatcher(new Constraint[]{new IsAnything()}),
                                            new IsSameAsProxyStub()));
        addInvokable(hiddenInvocationMocker("hashCode",
                                            NoArgumentsMatcher.INSTANCE,
                                            new HashCodeStub()));
    }

    private void forgetFirstFailure() {
        firstFailure = null;
    }

    private static final InvocationMocker.Describer NO_DESCRIPTION =
        new InvocationMocker.Describer()
        {
            public boolean hasDescription() {
                return false;
            }

            public void describeTo( StringBuffer buffer, List matchers, Stub stub, String name ) {
            }
        };

    private InvocationMocker hiddenInvocationMocker( String methodName,
                                                     InvocationMatcher arguments,
                                                     Stub stub )
    {
        InvocationMocker invocationMocker = new InvocationMocker(NO_DESCRIPTION);

        invocationMocker.addMatcher(new MethodNameMatcher(methodName));
        invocationMocker.addMatcher(arguments);
        invocationMocker.setStub(stub);

        return invocationMocker;
    }

    private class IsSameAsProxyStub extends CustomStub
    {
        private IsSameAsProxyStub() {
            super("returns whether equal to proxy");
        }

        public Object invoke( Invocation invocation ) throws Throwable {
            return new Boolean(invocation.parameterValues.get(0) == proxy());
        }
    }

    private class HashCodeStub extends CustomStub
    {
        private HashCodeStub() {
            super("returns hashCode for proxy");
        }

        public Object invoke( Invocation invocation ) throws Throwable {
            return new Integer(AbstractDynamicMock.this.hashCode());
        }
    }
}
