/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;


public interface InvocationDispatcher
        extends Verifiable, SelfDescribing
{
    void setupDefaultBehaviour(String name, Object proxy);
    
    Object dispatch( Invocation invocation ) throws Throwable;

    void setDefaultStub( Stub newDefaultStub );

    void add( Invokable invokable );

    void clear();
}
