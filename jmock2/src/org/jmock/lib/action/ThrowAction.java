/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.lib.action;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

/**
 * Throws an exception.
 * 
 * @author nat
 *
 */
public class ThrowAction implements Action {
    private Throwable throwable;
    
    public ThrowAction(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object invoke(Invocation invocation) throws Throwable {
        if (isThrowingCheckedException()) {
            checkTypeCompatiblity(invocation.getInvokedMethod().getExceptionTypes());
        }

        throwable.fillInStackTrace();
        throw throwable;
    }

    public void describeTo(Description description) {
        description.appendText("throws ");
        description.appendValue(throwable);
    }

    private void checkTypeCompatiblity(Class<?>[] allowedExceptionTypes) {
        for (int i = 0; i < allowedExceptionTypes.length; i++) {
            if (allowedExceptionTypes[i].isInstance(throwable))
                return;
        }
        
        reportIncompatibleCheckedException(allowedExceptionTypes);
    }

    private void reportIncompatibleCheckedException(Class<?>[] allowedTypes) {
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
        
        throw new IllegalStateException(message.toString());
    }
    
    private boolean isThrowingCheckedException() {
        return !(throwable instanceof RuntimeException || throwable instanceof Error);
    }
}
