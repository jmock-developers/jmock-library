/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.testsupport;

import junit.framework.AssertionFailedError;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.Invokable;
import org.jmock.expectation.*;
import org.jmock.util.*;

public class MockCallable implements Invokable {

    final public String name;

    public ExpectationValue callInvocation = new ExpectationValue("call");

    private ReturnValue callResult = new ReturnValue("call.return");
    private Throwable callThrow = null;

    private ExpectationValue matchesMethodName = new org.jmock.expectation.ExpectationValue("matches.methodName");
    private ExpectationList matchesArgs = new ExpectationList("matches.args");
    public boolean matches = false;
    private ExpectationCounter matchesCount = new ExpectationCounter("matches.count");

    private ExpectationCounter verifyCount = new ExpectationCounter("verify.count");
    private AssertionFailedError verifyError = null;

    public MockCallable(String name) {
        this.name = name;
    }

    public void setupCallReturn(Object result) {
        callResult.setValue(result);
    }

    public void setupCallThrow(Throwable thrown) {
        callThrow = thrown;
    }

    public Object invoke(Invocation anInvocation) throws Throwable {
        callInvocation.setActual(anInvocation);

        if (callThrow != null) {
            throw callThrow;
        } else {
            return callResult.getValue();
        }
    }

    public void setExpectedMatches(String methodName, Object[] args) {
        matchesMethodName.setExpected(methodName);
        matchesArgs.addExpectedMany(args);
    }

    public void setExpectedMatchesCount(int count) {
        matchesCount.setExpected(count);
    }

    public boolean matches(Invocation invocation) {
        matchesMethodName.setActual(invocation.getMethodName());
        matchesArgs.addActualMany(invocation.getParameterValues().toArray());
        matchesCount.inc();
        return matches;
    }

    public void setExpectedVerifyCalls(int count) {
        verifyCount.setExpected(count);
    }

    public void setupVerifyThrow(AssertionFailedError err) {
        verifyError = err;
    }

    /**
     * @deprecated to avoid calling verify instead of verifyExpectations
     */
    public void verify() {
        verifyCount.inc();
        if (verifyError != null) throw verifyError;
    }

    /**
     * We have to rename 'verify' because we want to mock the behaviour of the
     * verify method itself.
     */
    public void verifyExpectations() {
        Verifier.verifyObject(this);
    }

    public String toString() {
        return "MockCallable " + name;
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        throw new AssertionError("should implement writeTo");
    }
}
