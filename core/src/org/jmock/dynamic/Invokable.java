/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import org.jmock.SelfDescribing;
import org.jmock.Verifiable;

public interface Invokable 
    extends Verifiable, SelfDescribing 
{
    boolean matches(Invocation invocation);
    Object invoke(Invocation invocation) throws Throwable;
    
    boolean hasDescription();
}
