/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.dynamic.InvocationMatcher;

public interface ExpectationBuilder {
	ExpectationBuilder id( String id );
	
	ExpectationBuilder addExpectation( InvocationMatcher expectation );
	
	ExpectationBuilder expectOnce();
	
	ExpectationBuilder after( String previousCallID );
	//TODO after( Mock otherMock, String previousCallID );
	
    //TODO expectAtLeastOnce();
}
