/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.testsupport;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.Invokable;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.ExpectationList;
import org.jmock.expectation.ExpectationValue;
import org.jmock.expectation.ReturnValue;

public class MockCallable 
    extends MockVerifiable
    implements Invokable 
{

    final public String name;

    public ExpectationValue callInvocation = new ExpectationValue("call");

    private ReturnValue callResult = new ReturnValue("call.return");
    private Throwable callThrow = null;

    private ExpectationValue matchesMethodName = new org.jmock.expectation.ExpectationValue("matches.methodName");
    private ExpectationList matchesArgs = new ExpectationList("matches.args");
    public boolean matches = false;
    private ExpectationCounter matchesCount = new ExpectationCounter("matches.count");

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

    public String toString() {
        return "MockCallable " + name;
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        throw new AssertionError("should implement writeTo");
    }
}
