package org.jmock.internal;

import org.jmock.core.Action;
import org.jmock.core.Expectation;

public interface ExpectationBuilder {
    void setDefaultAction(Action defaultAction);
    Expectation toExpectation();
}
