package org.jmock.internal;

import org.jmock.api.Invokable;

public class ObjectMethodExpectationBouncer extends FakeObjectMethods {
    public ObjectMethodExpectationBouncer(Invokable next) {
        super(next);
    }

    @Override
    protected boolean fakeEquals(Object invokedObject, Object other) {
        throw cannotDefineExpectation();
    }

    @Override
    protected void fakeFinalize(Object invokedObject) {
        throw cannotDefineExpectation();
    }

    @Override
    protected int fakeHashCode(Object invokedObject) {
        throw cannotDefineExpectation();
    }

    @Override
    protected String fakeToString(Object invokedObject) {
        throw cannotDefineExpectation();
    }
    
    private IllegalArgumentException cannotDefineExpectation() {
        return new IllegalArgumentException("you cannot define expectations for methods defined by the Object class");
    }
}
