/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.testsupport;

import junit.framework.AssertionFailedError;

import org.jmock.dynamic.framework.Invocation;
import org.jmock.dynamic.framework.InvocationDispatcher;
import org.jmock.dynamic.framework.Invokable;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationValue;
import org.jmock.expectation.MockObject;

public class MockInvocationDispatcher
        extends MockObject
        implements InvocationDispatcher {
    public ExpectationValue dispatchInvocation = new ExpectationValue("dispatchInvocation");
    public Object dispatchResult;
    public Throwable dispatchThrowable;
    public ExpectationValue addInvokable = new ExpectationValue("addInvokable");
    public ExpectationCounter clearCalls = new ExpectationCounter("clear calls");
    public ExpectationCounter verifyCalls = new ExpectationCounter("verify calls");
    public AssertionFailedError verifyFailure;
    public String writeToOutput = "MockInvocationDispatcher.writeTo output";

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

    /**
     * @deprecated Use verifyExpectations to verify this object
     */
    public void verify() {
        verifyCalls.inc();
        if (null != verifyFailure) {
            throw verifyFailure;
        }
    }


    public void verifyExpectations() {
        super.verify();
    }

    public void writeTo(StringBuffer buffer) {
    	buffer.append( writeToOutput );
    }
}
