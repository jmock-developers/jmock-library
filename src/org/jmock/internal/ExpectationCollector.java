package org.jmock.internal;

import org.jmock.api.Expectation;

public interface ExpectationCollector {
    void add(Expectation expectation);
}
