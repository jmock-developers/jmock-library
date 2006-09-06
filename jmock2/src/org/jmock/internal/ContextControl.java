package org.jmock.internal;

import org.jmock.core.Expectation;


public interface ContextControl {
    void startCapturingExpectations(ExpectationCapture capture);
    void setExpectation(Expectation expectation);
}
