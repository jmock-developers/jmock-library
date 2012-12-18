package org.jmock.test.unit.internal;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.jmock.api.Invocation;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectation;
import org.jmock.internal.OrderingConstraint;
import org.jmock.internal.SideEffect;
import org.jmock.internal.matcher.AllParametersMatcher;
import org.jmock.lib.action.ReturnValueAction;
import org.jmock.test.unit.support.AssertThat;
import org.jmock.test.unit.support.MethodFactory;
import org.jmock.test.unit.support.MockAction;

public class InvocationExpectationTests extends TestCase {
	MethodFactory methodFactory = new MethodFactory();
	InvocationExpectation expectation = new InvocationExpectation();
	Object targetObject = "targetObject";
	Method method = methodFactory.newMethod("method");
	
	public <T> Matcher<T> mockMatcher(final T expected, final boolean result) {
		return new BaseMatcher<T>() {
			public boolean matches(Object actual) {
				assertTrue("expected " + expected + ", was " + actual,
				           equalTo(expected).matches(actual));
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
		
		expectation.setObjectMatcher(sameInstance(targetObject));
		
		assertTrue("should match", expectation.matches(new Invocation(targetObject, method, Invocation.NO_PARAMETERS)));
		assertTrue("should not match", !expectation.matches(new Invocation(anotherObject, method, Invocation.NO_PARAMETERS)));
	}
	
	public void testCanConstrainMethod() {
		Method anotherMethod = methodFactory.newMethod("anotherMethod");
		
		expectation.setMethodMatcher(equalTo(method));
		
		assertTrue("should match", expectation.matches(new Invocation(targetObject, method, Invocation.NO_PARAMETERS)));
		assertTrue("should not match", !expectation.matches(new Invocation(targetObject, anotherMethod, Invocation.NO_PARAMETERS)));
	}
	
	public void testCanConstrainArguments() {
		Object[] args = {1,2,3,4};
		Object[] differentArgs = {5,6,7,8};
		Object[] differentArgCount = {1,2,3};
		Object[] noArgs = null;
		
		expectation.setParametersMatcher(new AllParametersMatcher(args));
		
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
			assertFalse("should not be satisfied after " + i +" invocations",
					expectation.isSatisfied());
			
			expectation.invoke(invocation);
		}

		assertTrue("should match after " + i +" invocations", 
		           expectation.matches(invocation));
		assertTrue("should be satisfied after " + i +" invocations",
		           expectation.isSatisfied());
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
    
    public void testPerformsSideEffectsWhenInvoked() throws Throwable {
        FakeSideEffect sideEffect1 = new FakeSideEffect();
        FakeSideEffect sideEffect2 = new FakeSideEffect();
        
        expectation.addSideEffect(sideEffect1);
        expectation.addSideEffect(sideEffect2);
        
        Invocation invocation = new Invocation("targetObject", methodFactory.newMethod("method"));
        expectation.invoke(invocation);
        
        assertTrue("side effect 1 should have been performed", sideEffect1.wasPerformed);
        assertTrue("side effect 2 should have been performed", sideEffect2.wasPerformed);
    }
    
    public void testDescriptionIncludesSideEffects() {
        FakeSideEffect sideEffect1 = new FakeSideEffect();
        FakeSideEffect sideEffect2 = new FakeSideEffect();
        
        sideEffect1.descriptionText = "side-effect-1";
        sideEffect2.descriptionText = "side-effect-2";
        
        expectation.addSideEffect(sideEffect1);
        expectation.addSideEffect(sideEffect2);
        
        String description = StringDescription.toString(expectation);
        
        AssertThat.stringIncludes("should include description of sideEffect1",
                                  sideEffect1.descriptionText, description);
        AssertThat.stringIncludes("should include description of sideEffect2",
                                  sideEffect2.descriptionText, description);
        
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
        assertFalse(expectation.isSatisfied());
        
        expectation.invoke(invocation);
        expectation.invoke(invocation);
        
        assertFalse(expectation.allowsMoreInvocations());
        assertTrue(expectation.isSatisfied());
    }
    
    public void testMatchesIfAllOrderingConstraintsMatch() {
        FakeOrderingConstraint orderingConstraint1 = new FakeOrderingConstraint();
        FakeOrderingConstraint orderingConstraint2 = new FakeOrderingConstraint();
        
        expectation.addOrderingConstraint(orderingConstraint1);
        expectation.addOrderingConstraint(orderingConstraint2);
        
        Invocation invocation = new Invocation(targetObject, method, Invocation.NO_PARAMETERS);
        
        orderingConstraint1.allowsInvocationNow = true;
        orderingConstraint2.allowsInvocationNow = true;
        assertTrue(expectation.matches(invocation));
        
        orderingConstraint1.allowsInvocationNow = true;
        orderingConstraint2.allowsInvocationNow = false;
        assertFalse(expectation.matches(invocation));
        
        orderingConstraint1.allowsInvocationNow = false;
        orderingConstraint2.allowsInvocationNow = true;
        assertFalse(expectation.matches(invocation));
        
        orderingConstraint1.allowsInvocationNow = false;
        orderingConstraint2.allowsInvocationNow = false;
        assertFalse(expectation.matches(invocation));
    }
    
    public void testDescriptionIncludesCardinality() {
        final Cardinality cardinality = new Cardinality(2, 2);
        expectation.setCardinality(cardinality);
        
        AssertThat.stringIncludes("should include cardinality description",
                                  StringDescription.toString(cardinality), 
                                  StringDescription.toString(expectation));
    }
    
    public void testDescribesNumberOfInvocationsReceived() throws Throwable {
        Invocation invocation = new Invocation(targetObject, method, Invocation.NO_PARAMETERS);
        
        expectation.setCardinality(new Cardinality(2,3));
        
        AssertThat.stringIncludes("should describe as not invoked",
                                  "never invoked", StringDescription.toString(expectation));
        
        expectation.invoke(invocation);
        AssertThat.stringIncludes("should describe as invoked 1 time",
                                  "invoked 1 time", StringDescription.toString(expectation));
        
        expectation.invoke(invocation);
        expectation.invoke(invocation);
        AssertThat.stringIncludes("should describe as invoked 3 times",
                                  "invoked 3 times", StringDescription.toString(expectation));
    }

    public static class FakeOrderingConstraint implements OrderingConstraint {
        public boolean allowsInvocationNow;
        
        public boolean allowsInvocationNow() {
            return allowsInvocationNow;
        }

        public String descriptionText;
        
        public void describeTo(Description description) {
            description.appendText(descriptionText);
        }
    }
    
    class FakeSideEffect implements SideEffect {
        public boolean wasPerformed = false;
        
        public void perform() {
            wasPerformed = true;
        }

        public String descriptionText;
        
        public void describeTo(Description description) {
            description.appendText(descriptionText);
        }
    }
}
