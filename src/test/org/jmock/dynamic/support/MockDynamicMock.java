/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.support;

import org.jmock.dynamic.DynamicMock;
import org.jmock.dynamic.Invokable;
import org.jmock.expectation.AssertMo;
import org.jmock.expectation.ExpectationCounter;
import org.jmock.expectation.Verifier;

import java.lang.reflect.Method;

public class MockDynamicMock
        extends AssertMo
        implements DynamicMock {
    public ExpectationCounter addCalls = new ExpectationCounter("add calls");

    public void add(Invokable invokable) {
        assertNotNull("invokable", invokable);
        addCalls.inc();
    }

    public Object proxy() {
        return null;
    }

    public void reset() {
    }

    public ExpectationCounter verifyCalls = new ExpectationCounter("verify");

    public void verify() {
        verifyCalls.inc();
    }

    public Object invoke(Object arg0, Method arg1, Object[] arg2)
            throws Throwable {
        return null;
    }

    public String toStringResult;

    public String toString() {
        return toStringResult;
    }

    public void verifyExpectations() {
        Verifier.verifyObject(this);
    }
}
