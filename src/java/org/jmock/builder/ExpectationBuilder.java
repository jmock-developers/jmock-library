/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.dynamic.InvocationMatcher;

public interface ExpectationBuilder {
	ExpectationBuilder addExpectation( InvocationMatcher expectation );
	
	ExpectationBuilder expectOnce();

	ExpectationBuilder expectAfter(ExpectationBuilder previousCall );
	
    //TODO expectAtLeastOnce()
}
