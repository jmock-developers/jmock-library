/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.testsupport;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.InvocationDispatcher;
import org.jmock.dynamic.Invokable;
import org.jmock.dynamic.Stub;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationValue;

public class MockInvocationDispatcher
    extends MockVerifiable
    implements InvocationDispatcher 
{
    public ExpectationValue dispatchInvocation = new ExpectationValue("dispatchInvocation");
    public Object dispatchResult;
    public Throwable dispatchThrowable;
    public ExpectationValue addInvokable = new ExpectationValue("addInvokable");
    public ExpectationCounter clearCalls = new ExpectationCounter("clear calls");
    public String writeToOutput = "MockInvocationDispatcher.writeTo output";
    public ExpectationValue setDefaultStub = new ExpectationValue("setDefaultStub");
    
    
    public void setDefaultStub( Stub newDefaultStub ) {
        setDefaultStub.setActual(newDefaultStub);
    }
    
    public void add(Invokable invokable) {
        addInvokable.setActual(invokable);
    }

    public void clear() {
        clearCalls.inc();
    }

    public Object dispatch(Invocation invocation) throws Throwable {
        dispatchInvocation.setActual(invocation);
        if (null != dispatchThrowable) {
            throw dispatchThrowable;
        }
        return dispatchResult;
    }

    public StringBuffer describeTo(StringBuffer buffer) {
    	return buffer.append( writeToOutput );
    }
}
