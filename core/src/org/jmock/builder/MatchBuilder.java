/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.builder;

import org.jmock.core.InvocationMatcher;

/**
 * @since 1.0
 */
public interface MatchBuilder extends StubBuilder
{
    MatchBuilder match( InvocationMatcher customMatcher );

    MatchBuilder after( String previousCallID );

    MatchBuilder after( BuilderNamespace otherMock, String previousCallID );
}
