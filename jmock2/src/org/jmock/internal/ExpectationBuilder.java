package org.jmock.internal;

import org.jmock.api.Action;

public interface ExpectationBuilder {
    void buildExpectations(Action defaultAction, ExpectationCollector collector);
}
