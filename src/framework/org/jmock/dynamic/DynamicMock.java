/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import org.jmock.Verifiable;

import java.lang.reflect.InvocationHandler;

public interface DynamicMock
    extends Verifiable, InvocationHandler 
{
    Class getMockedType();
    
    Object proxy();
    
    void setDefaultStub( Stub newDefaultStub );
    
    void add(Invokable invokable);

    void reset();
}

