/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import org.jmock.expectation.Verifiable;

public interface InvocationMatcher 
	extends Verifiable 
{
    boolean matches(Invocation invocation);

    void invoked(Invocation invocation);

    StringBuffer writeTo(StringBuffer buffer);
}
