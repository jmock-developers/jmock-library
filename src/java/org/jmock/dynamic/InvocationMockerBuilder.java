/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.ThrowStub;
import org.jmock.dynamic.stub.VoidStub;

public class InvocationMockerBuilder implements StubBuilder, ExpectationBuilder {

    private InvocationMocker mocker;

    public InvocationMockerBuilder(InvocationMocker mocker) {
        this.mocker = mocker;
    }

    public ExpectationBuilder isVoid() {
        mocker.setStub(new VoidStub());
        return this;
    }

    public ExpectationBuilder returns(Object returnValue) {
        mocker.setStub(new ReturnStub(returnValue));
        return this;
    }

    public ExpectationBuilder willThrow(Throwable throwable) {
        mocker.setStub(new ThrowStub(throwable));
        return this;
    }
}
