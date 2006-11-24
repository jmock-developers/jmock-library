package org.jmock.test.unit.lib;

import junit.framework.TestCase;

import org.jmock.api.Invocation;
import org.jmock.lib.UnorderedExpectationGroup;
import org.jmock.test.unit.support.MethodFactory;
import org.jmock.test.unit.support.MockExpectation;

public class UnorderedExpectationGroupTests extends TestCase {
	MethodFactory methodFactory = new MethodFactory();
	Invocation invocation = new Invocation(
	    "invokedObject", 
		methodFactory.newMethod("invokedMethod"),
		Invocation.NO_PARAMETERS);
	
	static final boolean NOT_RELEVANT = true;
	
	public void testMatchesIfAnyExpectationsInGroupMatch() {
		UnorderedExpectationGroup groupAll = new UnorderedExpectationGroup();
		groupAll.add(new MockExpectation(true, NOT_RELEVANT, NOT_RELEVANT));
		groupAll.add(new MockExpectation(true, NOT_RELEVANT, NOT_RELEVANT));
		assertTrue("should match if all expectations in group match",
				   groupAll.matches(invocation));
		
		UnorderedExpectationGroup group1 = new UnorderedExpectationGroup();
		group1.add(new MockExpectation(true, NOT_RELEVANT, NOT_RELEVANT));
		group1.add(new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT));
		assertTrue("should match if one in group match (first)",
                   group1.matches(invocation));
		
		UnorderedExpectationGroup group2 = new UnorderedExpectationGroup();
		group2.add(new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT));
		group2.add(new MockExpectation(true, NOT_RELEVANT, NOT_RELEVANT));
		assertTrue("should match if one in group match (second)",
                   group2.matches(invocation));
        
		UnorderedExpectationGroup groupNone = new UnorderedExpectationGroup();
		groupNone.add(new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT));
		groupNone.add(new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT));
		assertTrue("should not match if none in group match",
                   !groupNone.matches(invocation));
	}
    
    public void testNeedsMoreInvocationsIfAnyInGroupNeedMoreInvocations() {
        UnorderedExpectationGroup groupAll = new UnorderedExpectationGroup();
        groupAll.add(new MockExpectation(NOT_RELEVANT, false, NOT_RELEVANT));
        groupAll.add(new MockExpectation(NOT_RELEVANT, false, NOT_RELEVANT));
        assertTrue("should be satisfied if all expectations in group are satisfied",
                   !groupAll.needsMoreInvocations());
        
        UnorderedExpectationGroup group1 = new UnorderedExpectationGroup();
        group1.add(new MockExpectation(NOT_RELEVANT, false, NOT_RELEVANT));
        group1.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        assertTrue("should not be satisfied if one in group is not satisfied (first)",
                   group1.needsMoreInvocations());
        
        UnorderedExpectationGroup group2 = new UnorderedExpectationGroup();
        group2.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        group2.add(new MockExpectation(NOT_RELEVANT, false, NOT_RELEVANT));
        assertTrue("should not be satisfied if one in group is not satisfied (second)",
                   group2.needsMoreInvocations());
        
        UnorderedExpectationGroup groupNone = new UnorderedExpectationGroup();
        groupNone.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        groupNone.add(new MockExpectation(NOT_RELEVANT, true, NOT_RELEVANT));
        assertTrue("should not be satisfied if none in group are satisfied",
                   groupNone.needsMoreInvocations());
    }
    
    public void testAllowsInvocationsIfAnyInGroupAllowInvocations() {
        UnorderedExpectationGroup groupAll = new UnorderedExpectationGroup();
        groupAll.add(new MockExpectation(NOT_RELEVANT, NOT_RELEVANT, true));
        groupAll.add(new MockExpectation(NOT_RELEVANT, NOT_RELEVANT, true));
        assertTrue("should be active if all expectations in group are active",
                   groupAll.allowsMoreInvocations());
        
        UnorderedExpectationGroup group1 = new UnorderedExpectationGroup();
        group1.add(new MockExpectation(NOT_RELEVANT, NOT_RELEVANT, false));
        group1.add(new MockExpectation(NOT_RELEVANT, NOT_RELEVANT, true));
        assertTrue("should be active if one in group is not active (first)",
                   group1.allowsMoreInvocations());
        
        UnorderedExpectationGroup group2 = new UnorderedExpectationGroup();
        group2.add(new MockExpectation(NOT_RELEVANT, NOT_RELEVANT, true));
        group2.add(new MockExpectation(NOT_RELEVANT, NOT_RELEVANT, false));
        assertTrue("should be active if one in group is not active (second)",
                   group2.allowsMoreInvocations());
        
        UnorderedExpectationGroup groupNone = new UnorderedExpectationGroup();
        groupNone.add(new MockExpectation(NOT_RELEVANT, NOT_RELEVANT, false));
        groupNone.add(new MockExpectation(NOT_RELEVANT, NOT_RELEVANT, false));
        assertTrue("should not be active if none in group are active",
                   !groupNone.allowsMoreInvocations());
    }
    
    public void testInvokesFirstMatchingExpectationInGroup() throws Throwable {
        MockExpectation expectation1 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation2 = new MockExpectation(true, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation3 = new MockExpectation(true, NOT_RELEVANT, NOT_RELEVANT);
        
        UnorderedExpectationGroup group = new UnorderedExpectationGroup();
        group.add(expectation1);
        group.add(expectation2);
        group.add(expectation3);
        
        expectation1.shouldNotBeInvoked();
        expectation2.shouldBeInvokedWith(invocation);
        expectation3.shouldNotBeInvoked();
        
        group.invoke(invocation);
        
        assertTrue("expectation2 should have been invoked", 
                   expectation2.wasInvoked);
    }

    public void testCanOnlyBeInvokedIfItMatchesTheInvocation() throws Throwable {
        MockExpectation expectation1 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation2 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);
        MockExpectation expectation3 = new MockExpectation(false, NOT_RELEVANT, NOT_RELEVANT);
        
        UnorderedExpectationGroup group = new UnorderedExpectationGroup();
        group.add(expectation1);
        group.add(expectation2);
        group.add(expectation3);
        
        expectation1.shouldNotBeInvoked();
        expectation2.shouldNotBeInvoked();
        expectation3.shouldNotBeInvoked();
        
        try {
            group.invoke(invocation);
            fail("should have thrown IllegalStateException");
        }
        catch (IllegalStateException ex) {
            // expected
        }
    }
}
