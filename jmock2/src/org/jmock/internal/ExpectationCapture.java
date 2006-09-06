package org.jmock.internal;

import org.jmock.core.Action;
import org.jmock.core.Invocation;


public interface ExpectationCapture {
    void setDefaultAction(Action defaultAction);
    void createExpectationFrom(Invocation invocation);
    void stopCapturingExpectations();
}
