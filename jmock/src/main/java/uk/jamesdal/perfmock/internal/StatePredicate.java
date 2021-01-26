package uk.jamesdal.perfmock.internal;

import org.hamcrest.SelfDescribing;

public interface StatePredicate extends SelfDescribing {
    boolean isActive();
}
