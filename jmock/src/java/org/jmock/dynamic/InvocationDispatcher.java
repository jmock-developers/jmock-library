/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import org.jmock.Verifiable;

public interface InvocationDispatcher extends Verifiable {
    Object dispatch(Invocation invocation) throws Throwable;
    
    Stub getDefaultStub();
    void setDefaultStub( Stub newDefaultStub );
    
    void add(Invokable invokable);

    void clear();

    void writeTo(StringBuffer buffer);
}
