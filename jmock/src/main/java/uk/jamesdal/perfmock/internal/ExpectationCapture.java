package uk.jamesdal.perfmock.internal;

import uk.jamesdal.perfmock.api.Invocation;


public interface ExpectationCapture {
    void createExpectationFrom(Invocation invocation);
}
