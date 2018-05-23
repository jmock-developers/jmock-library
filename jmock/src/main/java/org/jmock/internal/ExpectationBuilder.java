package org.jmock.internal;

import org.jmock.api.Action;
import org.jmock.api.ExpectationCollector;

public interface ExpectationBuilder {
    void buildExpectations(Action defaultAction, ExpectationCollector collector);
}
