package org.jmock.test.unit.lib;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsSame;
import org.jmock.core.Invocation;
import org.jmock.lib.InvocationExpectation;
import org.jmock.test.unit.support.AssertThat;
import org.jmock.test.unit.support.GetDescription;
import org.jmock.test.unit.support.MethodFactory;
import org.jmock.test.unit.support.MockAction;


public class InvocationExpectationTests extends TestCase {
	MethodFactory methodFactory = new MethodFactory();
	InvocationExpectation expectation = new InvocationExpectation();
	Object targetObject = "targetObject";
	Method method = methodFactory.newMethod("method");
	
	public <T> Matcher<T> mockMatcher(final T expected, final boolean result) {
		return new Matcher<T>() {
			public boolean matches(Object actual) {
				assertTrue(
					"expected " + expected + ", was " + actual,
					IsEqual.eq(expected).matches(actual));
				return result;
			}
			public void describeTo(Description description) {
			}
		};
	}
	
	public void testMatchesAnythingByDefault() {
		assertTrue("should match", expectation.matches(
				new Invocation(new Object(), methodFactory.newMethod("method"), Invocation.NO_PARAMETERS)));

		assertTrue("should match", expectation.matches(
				new Invocation(new Object(), methodFactory.newMethod("anotherMethod"), 
						       new Object[]{1,2,3,4})));
	}
	
	public void testCanConstrainTargetObject() {
		Object anotherObject = "anotherObject";
		
		expectation.setObjectMatcher(IsSame.same(targetObject));
		
		assertTrue("should match", expectation.matches(new Invocation(targetObject, method, Invocation.NO_PARAMETERS)));
		assertTrue("should not match", !expectation.matches(new Invocation(anotherObject, method, Invocation.NO_PARAMETERS)));
	}
	
	public void testCanConstrainMethod() {
		Method anotherMethod = methodFactory.newMethod("anotherMethod");
		
		expectation.setMethodMatcher(IsEqual.eq(method));
		
		assertTrue("should match", expectation.matches(new Invocation(targetObject, method, Invocation.NO_PARAMETERS)));
		assertTrue("should not match", !expectation.matches(new Invocation(targetObject, anotherMethod, Invocation.NO_PARAMETERS)));
	}
	
	public void testCanConstrainArguments() {
		Object[] args = {1,2,3,4};
		Object[] differentArgs = {5,6,7,8};
		Object[] differentArgCount = {1,2,3};
		Object[] noArgs = null;
		
		expectation.setParametersMatcher(IsEqual.eq(args));
		
		assertTrue("should match", expectation.matches(new Invocation(targetObject, method, args)));
		assertTrue("should not match", !expectation.matches(new Invocation(targetObject, method, differentArgs)));
		assertTrue("should not match", !expectation.matches(new Invocation(targetObject, method, differentArgCount)));
		assertTrue("should not match", !expectation.matches(new Invocation(targetObject, method, noArgs)));
	}
	
	public void testDoesNotMatchIfMatchingCountMatcherDoesNotMatch() throws Throwable {
		Invocation invocation = new Invocation("targetObject", methodFactory.newMethod("method"), Invocation.NO_PARAMETERS);
		
		int maxInvocationCount = 3;
        
        expectation.setCardinality(0, maxInvocationCount);
		
		int i;
		for (i = 0; i < maxInvocationCount; i++) {
			assertTrue("should match after " + i +" invocations", expectation.matches(invocation));
			expectation.invoke(invocation);
		}
		assertFalse("should not match after " + i + " invocations", expectation.matches(invocation));
	}
	
	public void testMustMeetTheRequiredInvocationCountButContinuesToMatch() throws Throwable {
		Invocation invocation = new Invocation("targetObject", methodFactory.newMethod("method"));
		
		int requiredInvocationCount = 3;
		expectation.setCardinality(requiredInvocationCount, Integer.MAX_VALUE);
		
		int i;
		for (i = 0; i < requiredInvocationCount; i++) {
			assertTrue("should match after " + i +" invocations", 
					expectation.matches(invocation));
			assertTrue("should not be satisfied after " + i +" invocations",
					expectation.needsMoreInvocations());
			
			expectation.invoke(invocation);
		}

		assertTrue("should match after " + i +" invocations", 
				expectation.matches(invocation));
		assertTrue("should be satisfied after " + i +" invocations",
				!expectation.needsMoreInvocations());
	}
    
    public void testPerformsActionWhenInvoked() throws Throwable {
        Invocation invocation = new Invocation(targetObject, method, Invocation.NO_PARAMETERS);
        MockAction action = new MockAction();
        
        action.expectInvoke = true;
        action.expectedInvocation = invocation;
        action.result = "result";
        
        expectation.setAction(action);
        
        Object actualResult = expectation.invoke(invocation);
        
        assertSame("actual result", action.result, actualResult);
        assertTrue("action1 was invoked", action.wasInvoked);
    }
    
    public void testReturnsNullIfHasNoActionsWhenInvoked() throws Throwable {
        Invocation invocation = new Invocation(targetObject, method, Invocation.NO_PARAMETERS);
        
        Object actualResult = expectation.invoke(invocation);
        
        assertNull("should have returned null", actualResult);
    }
    
    public void testHasARequiredAndMaximumNumberOfExpectedInvocations() throws Throwable {
        Invocation invocation = new Invocation(targetObject, method, Invocation.NO_PARAMETERS);
        
        expectation.setCardinality(2, 3);
        
        assertTrue(expectation.allowsMoreInvocations());
        assertTrue(expectation.needsMoreInvocations());
        
        expectation.invoke(invocation);

        assertTrue(expectation.allowsMoreInvocations());
        assertTrue(expectation.needsMoreInvocations());
        
        expectation.invoke(invocation);
        
        assertTrue(expectation.allowsMoreInvocations());
        assertFalse(expectation.needsMoreInvocations());
        
        expectation.invoke(invocation);
        
        assertFalse(expectation.allowsMoreInvocations());
        assertFalse(expectation.needsMoreInvocations());
    }
    
    public void testDescribesExactInvocationCount() {
        expectation.setCardinality(2, 2);
        
        AssertThat.stringIncludes("should describe exact invocation count",
                                  "exactly 2", GetDescription.of(expectation));
    }
    
    public void testDescribesAtLeastCount() {
        expectation.setCardinality(2, Integer.MAX_VALUE);
        
        AssertThat.stringIncludes("should describe at-least invocation count",
                                  "at least 2", GetDescription.of(expectation));
    }

    public void testDescribesAtMostCount() {
        expectation.setCardinality(0, 2);
        
        AssertThat.stringIncludes("should describe at-most invocation count",
                                  "at most 2", GetDescription.of(expectation));
    }
    
    public void testDescribesBetweenCount() {
        expectation.setCardinality(2, 4);
        
        AssertThat.stringIncludes("should describe between invocation count",
                                  "2 to 4", GetDescription.of(expectation));
    }

    public void testDescribesNeverCount() {
        expectation.setCardinality(0,0);
        
        AssertThat.stringIncludes("should describe 'never' invocation count",
                                  "never", GetDescription.of(expectation));
    }

    public void testDescribesAnyNumberCount() {
        expectation.setCardinality(0, Integer.MAX_VALUE);
        
        AssertThat.stringIncludes("should describe 'allowed' invocation count",
                                  "allowed", GetDescription.of(expectation));
        AssertThat.stringExcludes("should not include 'expected' in description",
                                  "expected", GetDescription.of(expectation));
    }
    
    public void testDescribesNumberOfInvocationsReceived() throws Throwable {
        Invocation invocation = new Invocation(targetObject, method, Invocation.NO_PARAMETERS);
        
        expectation.setCardinality(2,3);
        
        AssertThat.stringIncludes("should describe as not invoked",
                                  "invoked 0 times", GetDescription.of(expectation));
        
        expectation.invoke(invocation);
        AssertThat.stringIncludes("should describe as not invoked",
                                  "invoked 1 time", GetDescription.of(expectation));
        
        expectation.invoke(invocation);
        expectation.invoke(invocation);
        AssertThat.stringIncludes("should describe as not invoked",
                                  "invoked 3 times", GetDescription.of(expectation));
    }
}
