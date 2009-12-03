package org.jmock.internal;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.jmock.Sequence;
import org.jmock.api.Expectation;

/**
 * A sequence of expectations.
 * 
 * Invocations can be constrained to occur in strict order defined by a sequence.
 * 
 * @author nat
 */
public class NamedSequence implements Sequence {
    private final String name;
    private List<Expectation> elements = new ArrayList<Expectation>();
    
    public NamedSequence(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    public void constrainAsNextInSequence(InvocationExpectation expectation) {
        int index = elements.size();
        elements.add(expectation);
        expectation.addOrderingConstraint(new InSequenceOrderingConstraint(this, index));
    }
    
    private boolean isSatisfiedToIndex(int index) {
        for (int i = 0; i < index; i++) {
            if (!elements.get(i).isSatisfied()) return false;
        }
        return true;
    }
    
    private static class InSequenceOrderingConstraint implements OrderingConstraint {
        private final NamedSequence sequence;
        private final int index;

        public InSequenceOrderingConstraint(NamedSequence sequence, int index) {
            this.sequence = sequence;
            this.index = index;
        }

        public boolean allowsInvocationNow() {
            return sequence.isSatisfiedToIndex(index);
        }

        public void describeTo(Description description) {
            description.appendText("in sequence ").appendText(sequence.name);
        }
    }
}
