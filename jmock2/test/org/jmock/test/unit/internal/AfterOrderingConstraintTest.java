package org.jmock.test.unit.internal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.hamcrest.StringDescription;
import org.jmock.internal.AfterOrderingConstraint;
import org.jmock.internal.ExpectationNamespace;
import org.jmock.internal.OrderingConstraint;
import org.jmock.test.unit.support.AssertThat;
import org.jmock.test.unit.support.MockExpectation;

public class AfterOrderingConstraintTest extends TestCase {
    MockExpectation pre1 = new MockExpectation();
    MockExpectation pre2 = new MockExpectation();
    
    ExpectationNamespace namespace = new ExpectationNamespace();
    {
        namespace.bind("pre1", pre1);
        namespace.bind("pre2", pre2);
    }
    
    OrderingConstraint constraint = new AfterOrderingConstraint(namespace, setOf("pre1", "pre2")); 
    
    
    public void testAllowsTheInvocationIfNamedExpectationsHaveBeenInvokedAndAreSatisfied() {
        pre1.hasBeenInvoked = true;
        pre1.isSatisfied = true;
        pre2.hasBeenInvoked = true;
        pre2.isSatisfied = true;
        
        assertTrue(constraint.allowsInvocationNow());
    }
    
    public void testDisallowsTheInvocationIfNamedExpectationsHaveNotBeenSatisfied() {
        pre1.hasBeenInvoked = true;
        pre2.hasBeenInvoked = true;
        
        pre1.isSatisfied = false;
        pre2.isSatisfied = true;
        
        assertFalse(constraint.allowsInvocationNow());

        pre1.isSatisfied = true;
        pre2.isSatisfied = false;
        
        assertFalse(constraint.allowsInvocationNow());

        pre1.isSatisfied = false;
        pre2.isSatisfied = false;
        
        assertFalse(constraint.allowsInvocationNow());
    }
    
    public void testDisallowsTheInvocationIfNamedExpectationsHaveNotBeenInvokedEvenIfTheyAreSatisfied() {
        pre1.isSatisfied = true;
        pre2.isSatisfied = true;
        
        pre1.hasBeenInvoked = false;
        pre2.hasBeenInvoked = true;
        
        assertFalse(constraint.allowsInvocationNow());

        pre1.hasBeenInvoked = true;
        pre2.hasBeenInvoked = false;
        
        assertFalse(constraint.allowsInvocationNow());

        pre1.hasBeenInvoked = false;
        pre2.hasBeenInvoked = false;
        
        assertFalse(constraint.allowsInvocationNow());
    }
    
    public void testDescribesItself() {
        String description = StringDescription.toString(constraint);
        
        AssertThat.stringIncludes("should include 'after'", "after", description);
        AssertThat.stringIncludes("should include 'pre1'", "pre1", description);
        AssertThat.stringIncludes("should include 'pre2'", "pre2", description);
    }
    
    private <T> Set<T> setOf(T... elements) {
        return new HashSet<T>(Arrays.asList(elements));
    }
}
