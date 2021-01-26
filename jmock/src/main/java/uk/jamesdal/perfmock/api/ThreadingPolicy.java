package uk.jamesdal.perfmock.api;

public interface ThreadingPolicy {
    Invokable synchroniseAccessTo(Invokable mockObject);

    InvocationDispatcher dispatcher();
}
