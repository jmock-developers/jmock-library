/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.testsupport;

import java.lang.reflect.Method;
import org.jmock.core.DynamicMock;
import org.jmock.core.Invokable;
import org.jmock.core.Stub;
import org.jmock.expectation.AssertMo;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationValue;


public class MockDynamicMock
        extends MockVerifiable
        implements DynamicMock
{
    public ExpectationCounter getMockedTypeCalls =
            new ExpectationCounter("getMockedTypes #calls");
    public Class getMockedTypeResult;

    public Class getMockedType() {
        getMockedTypeCalls.inc();
        return getMockedTypeResult;
    }

    public ExpectationCounter addInvokableCalls = new ExpectationCounter("addInvokable #calls");
    public ExpectationValue addInvokable = new ExpectationValue("addInvokable invokable");

    public void addInvokable( Invokable invokable ) {
        AssertMo.assertNotNull("invokable", invokable);
        addInvokable.setActual(invokable);
        addInvokableCalls.inc();
    }

    public ExpectationValue setDefaultStub = new ExpectationValue("setDefaultStub");

    public void setDefaultStub( Stub newDefaultStub ) {
        setDefaultStub.setActual(newDefaultStub);
    }

    public Object proxyResult;

    public Object proxy() {
        return proxyResult;
    }

    public ExpectationCounter resetCalls = new ExpectationCounter("reset #calls");

    public void reset() {
        resetCalls.inc();
    }

    public Object invoke( Object arg0, Method arg1, Object[] arg2 )
            throws Throwable {
        return null;
    }

    public String toStringResult;

    public String toString() {
        return toStringResult;
    }
}
