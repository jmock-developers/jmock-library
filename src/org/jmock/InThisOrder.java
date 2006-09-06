package org.jmock;

import org.jmock.lib.OrderedExpectationGroup;

public class InThisOrder extends OrderingConstraint {
    public InThisOrder() {
        super(new OrderedExpectationGroup());
    }
}
