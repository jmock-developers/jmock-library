package org.jmock.internal;

import org.jmock.api.Action;
import org.jmock.api.Expectation;

public interface ExpectationBuilder {
    void setDefaultAction(Action defaultAction);
    Expectation toExpectation();
}
