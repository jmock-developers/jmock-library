/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.stub;

import org.jmock.dynamic.Invocation;

public class ThrowStub
        extends CallStub {
    private Throwable throwable;

    public ThrowStub(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        throw throwable;
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        return buffer.append("throws <").append(throwable).append(">");
    }
}
