package org.jmock.test.unit.internal;

import junit.framework.TestCase;

import org.hamcrest.StringDescription;
import org.jmock.internal.Cardinality;
import org.jmock.test.unit.support.AssertThat;

public class CardinalityTests extends TestCase {
    public void testDescribesOnceCardinality() {
        AssertThat.stringIncludes("should describe exact invocation count",
                                  "once", StringDescription.toString(new Cardinality(1, 1)));
    }

    public void testDescribesExactCardinality() {
        AssertThat.stringIncludes("should describe exact invocation count",
                                  "exactly 2", StringDescription.toString(new Cardinality(2, 2)));
    }

    public void testDescribesAtLeastCount() {
        AssertThat.stringIncludes("should describe at-least invocation count",
                                  "at least 2", 
                                  StringDescription.toString(new Cardinality(2, Integer.MAX_VALUE)));
    }

    public void testDescribesAtMostCount() {
        AssertThat.stringIncludes("should describe at-most invocation count",
                                  "at most 2", StringDescription.toString(new Cardinality(0, 2)));
    }

    public void testDescribesBetweenCount() {
        AssertThat.stringIncludes("should describe between invocation count",
                                  "2 to 4", StringDescription.toString(new Cardinality(2, 4)));
    }

    public void testDescribesNeverCount() {
        AssertThat.stringIncludes("should describe 'never' invocation count",
                                  "never", StringDescription.toString(new Cardinality(0,0)));
    }

    public void testDescribesAnyNumberCount() {
        final Cardinality allowed = new Cardinality(0, Integer.MAX_VALUE);
        
        AssertThat.stringIncludes("should describe 'allowed' invocation count",
                                  "allowed", StringDescription.toString(allowed));
        AssertThat.stringExcludes("should not include 'expected' in description",
                                  "expected", StringDescription.toString(allowed));
    }

    public void testHasARequiredAndMaximumNumberOfExpectedInvocations() throws Throwable {
        Cardinality cardinality = new Cardinality(2, 3);
        
        assertTrue(cardinality.allowsMoreInvocations(0));
        assertFalse(cardinality.isSatisfied(0));
        
        assertTrue(cardinality.allowsMoreInvocations(1));
        assertFalse(cardinality.isSatisfied(1));
        
        assertTrue(cardinality.allowsMoreInvocations(2));
        assertTrue(cardinality.isSatisfied(2));
        
        assertFalse(cardinality.allowsMoreInvocations(3));
        assertTrue(cardinality.isSatisfied(3));
    }

}
