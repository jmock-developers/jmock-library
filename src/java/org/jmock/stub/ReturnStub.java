/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.stub;

import org.jmock.Stub;
import org.jmock.dynamic.Invocation;

public class ReturnStub
        extends CallStub
        implements Stub 
{
    private Object result;

    public ReturnStub(Object result) {
        this.result = result;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        return result;
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        return buffer.append("returns <").append(result).append(">");
    }
}
