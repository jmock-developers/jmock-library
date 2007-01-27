package org.jmock.internal;



public interface CaptureControl {
    void startCapturingExpectations(ExpectationCapture capture);
    void stopCapturingExpectations();
}
