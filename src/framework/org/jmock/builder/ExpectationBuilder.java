/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.dynamic.InvocationMatcher;

public interface ExpectationBuilder {
	ExpectationBuilder id( String id );
    
    ExpectationBuilder after( String previousCallID );
    ExpectationBuilder after( BuilderIdentityTable otherMock, String previousCallID );
	
    ExpectationBuilder expect( InvocationMatcher expectation );
    
    /**
     * @deprecated use expect(expectation). Will be removed in version 1.0.
     */
	ExpectationBuilder addExpectation( InvocationMatcher expectation );
	
    /**
     * @deprecated use mock.expect(once())... Will be removed in version 1.0.
     */
	ExpectationBuilder expectOnce();
    
    /**
     * @deprecated use mock.expect(atLeastOnce())... Will be removed in version 1.0.
     */
	ExpectationBuilder expectAtLeastOnce();
	
    /**
     * @deprecated use mock.expect(notCalled())... Will be removed in version 1.0.
     */
    ExpectationBuilder expectNotCalled();
}
