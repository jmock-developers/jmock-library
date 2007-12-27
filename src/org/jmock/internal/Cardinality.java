package org.jmock.internal;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;

/**
 * The acceptable range of times an expectation may be invoked.
 * 
 * @author smgf
 * @author nat
 */
public class Cardinality implements SelfDescribing {
    public static final Cardinality ALLOWING = atLeast(0);
    
    private final int required;
    private final int maximum;
    
    public Cardinality(final int required, final int maximum) {
        this.required = required;
        this.maximum = maximum;
    }
    
    public static Cardinality exactly(int count) {
        return between(count, count);
    }

    public static Cardinality atLeast(int required) {
        return between(required, Integer.MAX_VALUE);
    }

    public static Cardinality between(int required, int maximum) {
        return new Cardinality(required, maximum);
    }

    public static Cardinality atMost(int maximum) {
        return between(0, maximum);
    }

    public boolean isSatisfied(int invocationsSoFar) {
        return required <= invocationsSoFar;
    }

    public boolean allowsMoreInvocations(int invocationCount) {
        return invocationCount < maximum;
    }

    public void describeTo(Description description) {
        if (required == 0 && maximum == Integer.MAX_VALUE) {
            description.appendText("allowed");
        }
        else {
            description.appendText("expected ");
            
            if (required == 0 && maximum == 0) {
                description.appendText("never");
            }
            else if (required == 1 && maximum == 1) {
                description.appendText("once");
            }
            else if (required == maximum) {
                description.appendText("exactly ");
                description.appendText(Formatting.times(required));
            }
            else if (maximum == Integer.MAX_VALUE) {
                description.appendText("at least ");
                description.appendText(Formatting.times(required));
            }
            else if (required == 0) {
                description.appendText("at most ");
                description.appendText(Formatting.times(maximum));
            }
            else {
                description.appendText(Integer.toString(required));
                description.appendText(" to ");
                description.appendText(Formatting.times(maximum));
            }
        }
    }
}
