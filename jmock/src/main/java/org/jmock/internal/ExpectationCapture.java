package org.jmock.internal;

import org.jmock.api.Invocation;


public interface ExpectationCapture {
    void createExpectationFrom(Invocation invocation);
}
