package org.jmock.test.unit.lib;

import junit.framework.TestCase;

import org.jmock.api.Invocation;
import org.jmock.lib.OrderedExpectationGroup;
import org.jmock.test.unit.support.MethodFactory;
import org.jmock.test.unit.support.MockExpectation;

public class OrderedExpectationGroupTests extends TestCase {
    MethodFactory methodFactory = new MethodFactory();
    Invocation invocation = new Invocation(
        "invokedObject", 
        methodFactory.newMethod("invokedMethod"),
        Invocation.NO_PARAMETERS);
    
    static final boolean NOT_RELEVANT = true;

    MockExpectation expectation1 = new MockExpectation();
    MockExpectation expectation2 = new MockExpectation();
    MockExpectation expectation3 = new MockExpectation();
    MockExpectation expectation4 = new MockExpectation();
    
    OrderedExpectationGroup group = new OrderedExpectationGroup();

    @Override
    public void setUp() {
        group.add(expectation1);
        group.add(expectation2);
        group.add(expectation3);
        group.add(expectation4);
        
        expectation1.invokeResult = 1;
        expectation2.invokeResult = 2;
        expectation3.invokeResult = 3;
        expectation4.invokeResult = 4;
    }
    
    public void testMatchesIfFirstMemberMatches() {
        expectation1.matches = true;
        expectation2.matches = false;
        
        assertTrue("group should match", group.matches(invocation));
    }
    
    public void testDoesNotMatchIfFirstMemberDoesNotMatchAndIsNotSatisfied() {
        expectation1.matches = false;
        expectation1.isSatisfied = false;
        
        assertTrue("group should not match", !group.matches(invocation));
    }
    
    public void testMatchesIfFirstMemberDoesNotMatchButFirstMemberIsSatisfiedAndNextMemberMatches() {
        expectation1.matches = false;
        expectation1.isSatisfied = true;
        expectation2.matches = true;
        expectation2.isSatisfied = false;
        
        assertTrue("group should match", group.matches(invocation));
    }

    public void testIfFirstMemberDoesNotMatchSearchesForMatchingMemberUntilNonMatchingUnsatisfiedMemberFound() {
        expectation1.matches = false;
        expectation1.isSatisfied = true;
        expectation2.matches = false;
        expectation2.isSatisfied = true;
        expectation3.matches = true;
        expectation3.isSatisfied = false;
        expectation4.matches = true;
        expectation4.isSatisfied = false;
        
        assertTrue("group should match if expectation3 matches", 
                   group.matches(invocation));
        
        expectation3.matches = false;
        
        assertTrue("group should not match if expectation3 does not match", 
                   !group.matches(invocation));
    }
    
    public void testInvokesNextMatchingMember() throws Throwable {
        expectation1.matches = true;
        expectation1.isSatisfied = false;
        
        assertEquals(1, group.invoke(invocation));
        assertTrue("expectation1 was invoked", expectation1.wasInvoked);
        
        expectation1.wasInvoked = false;
        expectation1.matches = false;
        expectation1.isSatisfied = true;
        expectation2.matches = false;
        expectation2.isSatisfied = true;
        expectation3.matches = true;
        expectation3.isSatisfied = false;
        
        assertEquals(3, group.invoke(invocation));
        assertTrue("expectation1 was not invoked", !expectation1.wasInvoked);
        assertTrue("expectation2 was not invoked", !expectation2.wasInvoked);
        assertTrue("expectation3 was invoked", expectation3.wasInvoked);
        
        expectation3.wasInvoked = false;
        expectation3.matches = true;
        
        assertEquals(3, group.invoke(invocation));
        assertTrue("expectation3 was invoked", expectation3.wasInvoked);
        
        expectation3.wasInvoked = false;
        expectation3.matches = false;
        expectation3.isSatisfied = true;
        expectation4.matches = true;
        
        assertEquals(4, group.invoke(invocation));
        assertTrue("expectation3 was not invoked", !expectation3.wasInvoked);
        assertTrue("expectation4 was invoked", expectation4.wasInvoked);
    }
    
    public void testIsSatisfiedWhenAllMembersHaveBeenSatisfied() throws Throwable {
        expectation1.matches = true;
        
        group.invoke(invocation);
        assertFalse(group.isSatisfied());
        
        expectation1.matches = false;
        expectation1.isSatisfied = true;
        expectation2.matches = true;
        
        group.invoke(invocation);
        assertFalse(group.isSatisfied());
        
        expectation2.matches = false;
        expectation2.isSatisfied = true;
        expectation3.matches = true;
        
        group.invoke(invocation);
        assertFalse(group.isSatisfied());

        expectation3.matches = false;
        expectation3.isSatisfied = true;
        expectation4.matches = true;
        
        group.invoke(invocation);
        assertFalse(group.isSatisfied());
        
        expectation4.isSatisfied = true;
        assertTrue(group.isSatisfied());
    }
    
    public void testIsSatisfiedIfAllUninvokedMatchersAreSatisfied() {
        expectation1.isSatisfied = true;
        expectation2.isSatisfied = true;
        expectation3.isSatisfied = true;
        expectation4.isSatisfied = true;
        
        assertTrue(group.isSatisfied()); 
    }
    
    public void testIsActiveIfAnyUninvokedMatcherIsActive() {
        expectation1.allowsMoreInvocations = false;
        expectation2.allowsMoreInvocations = true;
        expectation3.isSatisfied = true;
        expectation4.isSatisfied = true;
        
        assertTrue("group should be allow more invocations", group.allowsMoreInvocations());
    }
}
