/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core.stub;

import org.jmock.core.Invocation;
import org.jmock.core.Stub;

public class ThrowStub
    implements Stub
{
    private Throwable throwable;

    public ThrowStub(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        throw throwable;
    }

    public StringBuffer describeTo(StringBuffer buffer) {
        return buffer.append("throws <").append(throwable).append(">");
    }
}
