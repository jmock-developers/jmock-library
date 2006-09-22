package org.jmock;

import org.jmock.internal.ExpectationGroupBuilder;
import org.jmock.lib.UnorderedExpectationGroup;

/**
 * A block of expectations that can occur in any order relative to one another.
 */
public class InAnyOrder extends ExpectationGroupBuilder {
    public InAnyOrder() {
        super(new UnorderedExpectationGroup());
    }
}
