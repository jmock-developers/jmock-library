/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.dynamic.InvocationMatcher;

public interface MatchBuilder extends StubBuilder {
    MatchBuilder match( InvocationMatcher customMatcher );
    
	MatchBuilder after( String previousCallID );
    MatchBuilder after( BuilderIdentityTable otherMock, String previousCallID );
	
    /**
     * @deprecated use expect(expectation). Will be removed in version 1.0.
     */
	IdentityBuilder addExpectation( InvocationMatcher expectation );
	
    /**
     * @deprecated use mock.expect(once())... Will be removed in version 1.0.
     */
	IdentityBuilder expectOnce();
    
    /**
     * @deprecated use mock.expect(atLeastOnce())... Will be removed in version 1.0.
     */
	IdentityBuilder expectAtLeastOnce();
	
    /**
     * @deprecated use mock.expect(notCalled())... Will be removed in version 1.0.
     */
    IdentityBuilder expectNotCalled();
}
