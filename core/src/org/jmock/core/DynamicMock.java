/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core;


public interface DynamicMock
        extends Verifiable
{
    Class getMockedType();

    Object proxy();

    void setDefaultStub( Stub newDefaultStub );

    void addInvokable( Invokable invokable );

    void reset();
}

