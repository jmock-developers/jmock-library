/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.matcher;

import junit.framework.TestCase;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.matcher.MethodNameMatcher;
import test.jmock.dynamic.testsupport.MockConstraint;


public class MethodNameMatcherTest 
	extends TestCase 
{
	private String METHOD_NAME = "~method name~";
	private String OTHER_NAME = "~other name~";
	
	private Invocation exampleInvocation = new Invocation( 
        "INVOKED-OBJECT", Void.class, 
        METHOD_NAME, new Class[]{String.class}, Void.class, 
        new Object[]{"arg1", "arg2"});

	public void testDelegatesMethodNameMatchToConstraint() {
		MockConstraint mockConstraint = new MockConstraint("name constraint", METHOD_NAME, true );
		MethodNameMatcher matcher = new MethodNameMatcher(mockConstraint);
		
		assertTrue( "should match", matcher.matches(exampleInvocation) );
	}

	public void testDoesNotMatchIfConstraintFails() {
		MockConstraint mockConstraint = new MockConstraint("name constraint", METHOD_NAME, false );
		MethodNameMatcher matcher = new MethodNameMatcher(mockConstraint);
		
		assertFalse( "should not match", matcher.matches(exampleInvocation) );
	}
	
	public void testTestsMethodNameForEqualityToString() {
		MethodNameMatcher matcher;
		
		matcher = new MethodNameMatcher(METHOD_NAME);
		assertTrue("Should match name", matcher.matches(exampleInvocation));
		
		matcher = new MethodNameMatcher(OTHER_NAME);
		assertFalse("Should not match other name", matcher.matches(exampleInvocation));
	}
}
