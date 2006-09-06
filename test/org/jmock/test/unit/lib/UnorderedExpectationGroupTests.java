package org.jmock.test.unit.lib;

import junit.framework.TestCase;

import org.jmock.core.Invocation;
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
		groupAll.add(new MockExpectation(true, NOT_RELEVANT));
		groupAll.add(new MockExpectation(true, NOT_RELEVANT));
		assertTrue("should match if all expectations in group match",
				   groupAll.matches(invocation));
		
		UnorderedExpectationGroup group1 = new UnorderedExpectationGroup();
		group1.add(new MockExpectation(true, NOT_RELEVANT));
		group1.add(new MockExpectation(false, NOT_RELEVANT));
		assertTrue("should match if one in group match (first)",
                   group1.matches(invocation));
		
		UnorderedExpectationGroup group2 = new UnorderedExpectationGroup();
		group2.add(new MockExpectation(false, NOT_RELEVANT));
		group2.add(new MockExpectation(true, NOT_RELEVANT));
		assertTrue("should match if one in group match (second)",
                   group2.matches(invocation));
        
		UnorderedExpectationGroup groupNone = new UnorderedExpectationGroup();
		groupNone.add(new MockExpectation(false, NOT_RELEVANT));
		groupNone.add(new MockExpectation(false, NOT_RELEVANT));
		assertTrue("should not match if none in group match",
                   !groupNone.matches(invocation));
	}
    
    public void testIsSatisfiedIfAllInGroupAreSatisfied() {
        UnorderedExpectationGroup groupAll = new UnorderedExpectationGroup();
        groupAll.add(new MockExpectation(NOT_RELEVANT, true));
        groupAll.add(new MockExpectation(NOT_RELEVANT, true));
        assertTrue("should be satisfied if all expectations in group are satisfied",
                   groupAll.isSatisfied());
        
        UnorderedExpectationGroup group1 = new UnorderedExpectationGroup();
        group1.add(new MockExpectation(NOT_RELEVANT, false));
        group1.add(new MockExpectation(NOT_RELEVANT, true));
        assertTrue("should not be satisfied if one in group is not satisfied (first)",
                   !group1.isSatisfied());
        
        UnorderedExpectationGroup group2 = new UnorderedExpectationGroup();
        group2.add(new MockExpectation(NOT_RELEVANT, true));
        group2.add(new MockExpectation(NOT_RELEVANT, false));
        assertTrue("should not be satisfied if one in group is not satisfied (second)",
                   !group2.isSatisfied());
        
        UnorderedExpectationGroup groupNone = new UnorderedExpectationGroup();
        groupNone.add(new MockExpectation(NOT_RELEVANT, false));
        groupNone.add(new MockExpectation(NOT_RELEVANT, false));
        assertTrue("should not are satisfied if none in group are satisfied",
                   !groupNone.isSatisfied());
    }
    
    public void testInvokesFirstMatchingExpectationInGroup() throws Throwable {
        MockExpectation expectation1 = new MockExpectation(false, NOT_RELEVANT);
        MockExpectation expectation2 = new MockExpectation(true, NOT_RELEVANT);
        MockExpectation expectation3 = new MockExpectation(true, NOT_RELEVANT);
        
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
        MockExpectation expectation1 = new MockExpectation(false, NOT_RELEVANT);
        MockExpectation expectation2 = new MockExpectation(false, NOT_RELEVANT);
        MockExpectation expectation3 = new MockExpectation(false, NOT_RELEVANT);
        
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
