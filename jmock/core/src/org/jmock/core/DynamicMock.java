/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

/**
 * 
 * @since 1.0
 *
 */
public interface DynamicMock
    extends Verifiable
{
    Class getMockedType();

    Object proxy();

    void setDefaultStub( Stub newDefaultStub );

    void addInvokable( Invokable invokable );

    void reset();
}

