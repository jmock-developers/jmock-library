/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.testsupport;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.Invokable;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationValue;
import org.jmock.util.Verifier;

public class MockInvokable implements Invokable {

    public boolean matchesResult;
    public ExpectationValue matchesInvocation = new ExpectationValue("matches.invocation");

    public Object invokeResult;
    public ExpectationValue invokeInvocation = new ExpectationValue("invoke.invocation");
    public Throwable invokeThrow;
    public ExpectationCounter verifyCalls = new ExpectationCounter("verify.calls");


    public boolean matches(Invocation invocation) {
        matchesInvocation.setActual(invocation);
        return matchesResult;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        invokeInvocation.setActual(invocation);
        if (invokeThrow != null) {
            throw invokeThrow;
        }
        return invokeResult;
    }

    public void verify() {
        verifyCalls.inc();
    }

    public void verifyExpectations() {
        Verifier.verifyObject(this);
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        throw new AssertionError("should implement writeTo");
    }
}
