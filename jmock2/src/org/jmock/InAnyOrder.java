package org.jmock;

import org.jmock.internal.ExpectationGroupBuilder;
import org.jmock.lib.UnorderedExpectationGroup;

public class InAnyOrder extends ExpectationGroupBuilder {
    public InAnyOrder() {
        super(new UnorderedExpectationGroup());
    }
}
