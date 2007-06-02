/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;


public interface Invokable
        extends Verifiable, SelfDescribing
{
    boolean matches( Invocation invocation );

    Object invoke( Invocation invocation ) throws Throwable;

    boolean hasDescription();
}
