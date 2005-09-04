/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.stub;

import org.jmock.core.Formatting;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;

public class ReturnStub implements Stub {
    private Object result;

    public ReturnStub(Object result) {
        this.result = result;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        return result;
    }

    public StringBuffer describeTo(StringBuffer buffer) {
        return buffer.append("returns ").append(
                Formatting.toReadableString(result));
    }
}
