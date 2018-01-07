package org.jmock.api;

import org.jmock.internal.InvocationDispatcher;

public interface ThreadingPolicy {
    Invokable synchroniseAccessTo(Invokable mockObject);

    InvocationDispatcher buildDispatcher();
}
