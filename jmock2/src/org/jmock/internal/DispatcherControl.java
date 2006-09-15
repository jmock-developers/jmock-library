package org.jmock.internal;



public interface DispatcherControl {
    void startCapturingExpectations(ExpectationCapture capture);
    void stopCapturingExpectations();
}
