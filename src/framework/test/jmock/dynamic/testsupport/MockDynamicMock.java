/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.testsupport;

import java.lang.reflect.Method;

import org.jmock.dynamic.DynamicMock;
import org.jmock.dynamic.Invokable;
import org.jmock.dynamic.Stub;
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
    
    public ExpectationCounter addCalls = new ExpectationCounter("add #calls");
    public ExpectationValue addInvokable = new ExpectationValue("add invokable");

    public void add(Invokable invokable) {
        AssertMo.assertNotNull("invokable", invokable);
        addInvokable.setActual(invokable);
        addCalls.inc();
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

    public Object invoke(Object arg0, Method arg1, Object[] arg2)
            throws Throwable {
        return null;
    }
    
    public String toStringResult;

    public String toString() {
        return toStringResult;
    }
}
