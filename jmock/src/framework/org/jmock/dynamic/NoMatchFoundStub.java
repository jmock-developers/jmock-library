package org.jmock.dynamic;

import junit.framework.AssertionFailedError;

import org.jmock.dynamic.stub.CustomStub;


class NoMatchFoundStub extends CustomStub {
    public NoMatchFoundStub() {
        super("report no matching method");
    }
    public Object invoke( Invocation invocation ) throws Throwable {
		throw new AssertionFailedError("No match found");
    }
}