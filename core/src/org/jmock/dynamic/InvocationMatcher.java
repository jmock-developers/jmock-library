/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import org.jmock.SelfDescribing;
import org.jmock.Verifiable;

public interface InvocationMatcher 
	extends Verifiable, SelfDescribing
{
    boolean matches(Invocation invocation);
    void invoked(Invocation invocation);
    
    boolean hasDescription();
}
