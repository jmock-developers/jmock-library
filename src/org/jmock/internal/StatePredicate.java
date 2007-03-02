package org.jmock.internal;

import org.hamcrest.SelfDescribing;

public interface StatePredicate extends SelfDescribing {
    boolean isActive();
}
