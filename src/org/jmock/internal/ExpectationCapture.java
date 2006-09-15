package org.jmock.internal;

import org.hamcrest.Matcher;
import org.jmock.core.Action;
import org.jmock.core.Invocation;


public interface ExpectationCapture {
    void setDefaultAction(Action defaultAction);
    void addParameterMatcher(Matcher<?> matcher);
    void createExpectationFrom(Invocation invocation);
}
