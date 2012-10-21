package org.jmock.internal;


import org.jmock.api.Invokable;

public class ProxiedObjectIdentity extends FakeObjectMethods {
    public ProxiedObjectIdentity(Invokable next) {
        super(next);
    }
    
    @Override
    protected void fakeFinalize(Object invokedObject) {
    }

    @Override
    protected boolean fakeEquals(Object invokedObject, Object other) {
        return other == invokedObject;
    }

    @Override
    protected String fakeToString(Object invokedObject) {
        return toString();
    }

    @Override
    protected int fakeHashCode(Object invokedObject) {
        return System.identityHashCode(invokedObject);
    }
}
