package org.jmock;

import org.jmock.internal.ExpectationGroupBuilder;
import org.jmock.lib.OrderedExpectationGroup;

public class InThisOrder extends ExpectationGroupBuilder {
    public InThisOrder() {
        super(new OrderedExpectationGroup());
    }
}
