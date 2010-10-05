package org.jmock.api;

public interface ThreadingPolicy {
    Invokable synchroniseAccessTo(Invokable mockObject);
}
