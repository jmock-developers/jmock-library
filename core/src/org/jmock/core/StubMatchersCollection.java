/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

public interface StubMatchersCollection
{
    void setName( String name );

    void addMatcher( InvocationMatcher matcher );

    void setStub( Stub stub );
}
