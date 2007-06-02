/*  Copyright (c) 2000-2004 jMock.org
 */
package test.jmock.core.testsupport;

import org.jmock.core.Invocation;
import org.jmock.core.InvocationDispatcher;
import org.jmock.core.Invokable;
import org.jmock.core.Stub;
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
    public String writeToOutput = "MockInvocationDispatcher.describeTo output";
    public ExpectationValue setDefaultStub = new ExpectationValue("setDefaultStub");


    public void setDefaultStub( Stub newDefaultStub ) {
        setDefaultStub.setActual(newDefaultStub);
    }

    public void add( Invokable invokable ) {
        addInvokable.setActual(invokable);
    }

    public void clear() {
        clearCalls.inc();
    }

    public Object dispatch( Invocation invocation ) throws Throwable {
        if (invocation.invokedMethod.getName().equals("toString")) {
            return "MockInvocationDispatcher";
        }
        dispatchInvocation.setActual(invocation);
        if (null != dispatchThrowable) {
            throw dispatchThrowable;
        }
        return dispatchResult;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append(writeToOutput);
    }
}
