/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.dynamic.InvocationMatcher;

public interface ExpectationBuilder {
	ExpectationBuilder id( String id );
	
    ExpectationBuilder expect( InvocationMatcher expectation );
    
    ExpectationBuilder after( String previousCallID );
    ExpectationBuilder after( BuilderIdentityTable otherMock, String previousCallID );
    
    /**
     * @deprecated use expect(expectation). Will be removed in version 1.0.
     */
	ExpectationBuilder addExpectation( InvocationMatcher expectation );
	
    /**
     * @deprecated use expect(once()). Will be removed in version 1.0.
     */
	ExpectationBuilder expectOnce();
    
    /**
     * @deprecated use expect(atLeastOnce()). Will be removed in version 1.0.
     */
	ExpectationBuilder expectAtLeastOnce();
	
    /**
     * @deprecated use expect(notCalled()). Will be removed in version 1.0.
     */
    ExpectationBuilder expectNotCalled();
}
