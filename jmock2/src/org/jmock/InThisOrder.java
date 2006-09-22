package org.jmock;

import org.jmock.internal.ExpectationGroupBuilder;
import org.jmock.lib.OrderedExpectationGroup;

/**
 * A block of expectations that must occur, relative to one another, in the order in 
 * which they are defined
 */
public class InThisOrder extends ExpectationGroupBuilder {
    public InThisOrder() {
        super(new OrderedExpectationGroup());
    }
}
