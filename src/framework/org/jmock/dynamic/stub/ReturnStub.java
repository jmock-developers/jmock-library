/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.stub;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.Stub;

public class ReturnStub
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
