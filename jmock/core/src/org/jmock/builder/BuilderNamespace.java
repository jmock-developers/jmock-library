/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.builder;

/**
 * @since 1.0
 */
public interface BuilderNamespace
{
    MatchBuilder lookupID( String id );

    void registerMethodName( String id, MatchBuilder invocation );

    void registerUniqueID( String id, MatchBuilder invocation );
}
