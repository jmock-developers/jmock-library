/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import org.jmock.C;
import org.jmock.expectation.Verifiable;
import org.jmock.stub.VoidStub;

public class DynaMock
        implements Verifiable {
    DynamicMock coreMock;

    public DynaMock(Class mockedType) {
        this(new CoreMock(mockedType, CoreMock.mockNameFromClass(mockedType), new LIFOInvocationDispatcher()));
    }

    public DynaMock(DynamicMock coreMock) {
        this.coreMock = coreMock;
    }

    public Object proxy() {
        return coreMock.proxy();
    }

    public String toString() {
        return coreMock.toString();
    }

    public void verify() {
        coreMock.verify();
    }

    public StubBuilder method(String methodName, Object arg1, Object arg2) {
        InvocationMocker mocker = new InvocationMocker(methodName, C.eq(arg1, arg2), new VoidStub());
        coreMock.add(mocker);
        return new InvocationMockerBuilder(mocker);
    }
}
