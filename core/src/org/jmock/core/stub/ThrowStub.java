/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core.stub;

import org.jmock.core.Invocation;
import org.jmock.core.Stub;
import org.jmock.core.DynamicMockError;
import junit.framework.Assert;

import java.lang.reflect.Method;

public class ThrowStub
    extends Assert
    implements Stub
{
    private Throwable throwable;

    public ThrowStub(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object invoke(Invocation invocation) throws Throwable {
	    if( isThrowingCheckedException() ) {
		    checkTypeCompatiblity( invocation.invokedMethod.getExceptionTypes() );
	    }

        throw throwable;
    }

	public StringBuffer describeTo(StringBuffer buffer) {
        return buffer.append("throws <").append(throwable).append(">");
    }

	private void checkTypeCompatiblity(Class[] allowedTypes) {
		for (int i = 0; i < allowedTypes.length; i++) {
			if (allowedTypes[i].isInstance(throwable)) return;
		}

		reportTypeError(allowedTypes);
	}

	private void reportTypeError( Class[] allowedTypes ) {
		StringBuffer message = new StringBuffer();
		message.append("tried to throw an incompatible exception: expected one of ");
		for( int i = 0; i < allowedTypes.length; i++ ) {
			if( i > 0 ) message.append(",");
			message.append( allowedTypes[i].getName() );
		}
		message.append(" but threw a ");
		message.append( throwable.getClass().getName() );

		fail(message.toString());
	}

	private boolean isThrowingCheckedException() {
		return !(throwable instanceof RuntimeException || throwable instanceof Error);
	}


}
