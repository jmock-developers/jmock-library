/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.stub;

import junit.framework.Assert;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;

public class ThrowStub extends Assert implements Stub {
    private Throwable throwable;

    public ThrowStub(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        if (isThrowingCheckedException()) {
            checkTypeCompatiblity(invocation.invokedMethod.getExceptionTypes());
        }

        throwable.fillInStackTrace();
        throw throwable;
    }

    public StringBuffer describeTo(StringBuffer buffer) {
        return buffer.append("throws <").append(throwable).append(">");
    }

    private void checkTypeCompatiblity(Class[] allowedExceptionTypes) {
        for (int i = 0; i < allowedExceptionTypes.length; i++) {
            if (allowedExceptionTypes[i].isInstance(throwable))
                return;
        }

        reportIncompatibleCheckedException(allowedExceptionTypes);
    }

    private void reportIncompatibleCheckedException(Class[] allowedTypes) {
        StringBuffer message = new StringBuffer();

        message.append("tried to throw a ");
        message.append(throwable.getClass().getName());
        message.append(" from a method that throws ");

        if (allowedTypes.length == 0) {
            message.append("no exceptions");
        } else {
            for (int i = 0; i < allowedTypes.length; i++) {
                if (i > 0)
                    message.append(",");
                message.append(allowedTypes[i].getName());
            }
        }

        fail(message.toString());
    }

    private boolean isThrowingCheckedException() {
        return !(throwable instanceof RuntimeException || throwable instanceof Error);
    }
}