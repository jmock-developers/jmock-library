package org.jmock.internal;

import org.hamcrest.Matcher;
import org.jmock.core.Invocation;


public interface ExpectationCapture {
    void addParameterMatcher(Matcher<?> matcher);
    void createExpectationFrom(Invocation invocation);
}
