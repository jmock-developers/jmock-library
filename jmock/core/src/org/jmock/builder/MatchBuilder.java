/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.core.InvocationMatcher;

public interface MatchBuilder extends StubBuilder {
    MatchBuilder match( InvocationMatcher customMatcher );
    
	MatchBuilder after( String previousCallID );
    MatchBuilder after( BuilderIdentityTable otherMock, String previousCallID );
}
