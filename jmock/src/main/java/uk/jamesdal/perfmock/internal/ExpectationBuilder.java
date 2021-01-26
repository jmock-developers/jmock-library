package uk.jamesdal.perfmock.internal;

import uk.jamesdal.perfmock.api.Action;
import uk.jamesdal.perfmock.api.ExpectationCollector;

public interface ExpectationBuilder {
    void buildExpectations(Action defaultAction, ExpectationCollector collector);
}
