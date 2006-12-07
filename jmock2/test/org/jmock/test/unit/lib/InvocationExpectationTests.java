package org.jmock.test.unit.lib;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsSame;
import org.jmock.api.Invocation;
import org.jmock.lib.Cardinality;
import org.jmock.lib.InvocationExpectation;
import org.jmock.lib.action.ReturnValueAction;
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
        
        expectation.setCardinality(new Cardinality(0, maxInvocationCount));
		
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
		expectation.setCardinality(new Cardinality(requiredInvocationCount, Integer.MAX_VALUE));
		
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
        final Method stringReturningMethod = methodFactory.newMethod("tester", new Class[0], String.class, new Class[0]);
        Invocation invocation = new Invocation(targetObject, stringReturningMethod, Invocation.NO_PARAMETERS);
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
    

    public void testFailsIfActionReturnsAnIncompatibleValue() throws Throwable {
        final Method stringReturningMethod = methodFactory.newMethod("tester", new Class[0], String.class, new Class[0]);
        Invocation invocation = new Invocation(targetObject, stringReturningMethod, Invocation.NO_PARAMETERS);
        ReturnValueAction action = new ReturnValueAction(new Integer(666));
        expectation.setAction(action);
        
        try {
            expectation.invoke(invocation);
            fail("Should have thrown an IllegalStateException");
        } catch (IllegalStateException expected) {
            AssertThat.stringIncludes("Shows returned type", "java.lang.Integer", expected.getMessage());
            AssertThat.stringIncludes("Shows expected return type", "java.lang.String", expected.getMessage());
        }
    }
    
    /**
     * @see CardinalityTests.testHasARequiredAndMaximumNumberOfExpectedInvocations
     */
    public void testHasARequiredAndMaximumNumberOfExpectedInvocations() throws Throwable {
        Invocation invocation = new Invocation(targetObject, method, Invocation.NO_PARAMETERS);
        
        expectation.setCardinality(new Cardinality(1, 1));
        
        assertTrue(expectation.allowsMoreInvocations());
        assertTrue(expectation.needsMoreInvocations());
        
        expectation.invoke(invocation);
        expectation.invoke(invocation);
        
        assertFalse(expectation.allowsMoreInvocations());
        assertFalse(expectation.needsMoreInvocations());
    }
    
    public void testDescriptionIncludesCardinality() {
        final Cardinality cardinality = new Cardinality(2, 2);
        expectation.setCardinality(cardinality);
        
        AssertThat.stringIncludes("should include cardinality description",
                                  GetDescription.of(cardinality), 
                                  GetDescription.of(expectation));
    }

    
    public void testDescribesNumberOfInvocationsReceived() throws Throwable {
        Invocation invocation = new Invocation(targetObject, method, Invocation.NO_PARAMETERS);
        
        expectation.setCardinality(new Cardinality(2,3));
        
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
