/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core;


public interface InvocationDispatcher 
    extends Verifiable, SelfDescribing 
{
    Object dispatch(Invocation invocation) throws Throwable;
    
    void setDefaultStub( Stub newDefaultStub );
    
    void add(Invokable invokable);

    void clear();
}
