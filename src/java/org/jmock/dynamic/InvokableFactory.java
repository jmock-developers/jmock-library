/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import org.jmock.InvocationMatcher;
import org.jmock.matcher.CallOnceMatcher;
import org.jmock.stub.ReturnStub;
import org.jmock.stub.ThrowStub;
import org.jmock.stub.VoidStub;

public class InvokableFactory {

    public Invokable createReturnStub(String methodName, InvocationMatcher arguments, Object result) {
        return new InvocationMocker(methodName, arguments, new ReturnStub(result));
    }

    public Invokable createReturnExpectation(String methodName, InvocationMatcher arguments, Object result) {
        return callOnce(new InvocationMocker(methodName, arguments, new ReturnStub(result)));
    }

    public Invokable createThrowableStub(String methodName, InvocationMatcher arguments, Throwable throwable) {
        return new InvocationMocker(methodName, arguments, new ThrowStub(throwable));
    }

    public Invokable createThrowableExpectation(String methodName, InvocationMatcher arguments, Throwable throwable) {
        return callOnce(new InvocationMocker(methodName, arguments, new ThrowStub(throwable)));
    }

    public Invokable createVoidStub(String methodName, InvocationMatcher arguments) {
        return new InvocationMocker(methodName, arguments, new VoidStub());
    }

    public Invokable createVoidExpectation(String methodName, InvocationMatcher arguments) {
        return callOnce(new InvocationMocker(methodName, arguments, new VoidStub()));
    }

    private Invokable callOnce(InvocationMocker mocker) {
        return mocker.addMatcher(new CallOnceMatcher());
    }
}
