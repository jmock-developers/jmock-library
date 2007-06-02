/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

/**
 * @since 1.0
 */
public interface InvocationDispatcher
        extends Verifiable, SelfDescribing
{
    Object dispatch( Invocation invocation ) throws Throwable;

    void setDefaultStub( Stub newDefaultStub );

    void add( Invokable invokable );

    void clear();
}
