package org.jmock;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.jmock.api.Expectation;
import org.jmock.internal.InvocationExpectation;
import org.jmock.internal.OrderingConstraint;

public class Sequence {
    private final String name;
    private List<Expectation> elements = new ArrayList<Expectation>();
    
    public Sequence(String name) {
        this.name = name;
    }
    
    public void constrainAsNextInSequence(InvocationExpectation expectation) {
        int index = elements.size();
        elements.add(expectation);
        expectation.addOrderingConstraint(new InSequenceOrderingConstraint(this, index));
    }
    
    public boolean isSatisfiedToIndex(int index) {
        for (int i = 0; i < index; i++) {
            if (!elements.get(i).isSatisfied()) return false;
        }
        return true;
    }
    
    public static class InSequenceOrderingConstraint implements OrderingConstraint {
        private final Sequence sequence;
        private final int index;

        public InSequenceOrderingConstraint(Sequence sequence, int index) {
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
