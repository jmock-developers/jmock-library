/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.C;
import org.jmock.expectation.Verifiable;
import org.jmock.dynamic.framework.CoreMock;
import org.jmock.dynamic.framework.DynamicMock;
import org.jmock.dynamic.framework.InvocationMocker;
import org.jmock.dynamic.framework.LIFOInvocationDispatcher;
import org.jmock.dynamic.stub.VoidStub;

public class Mock
        implements Verifiable {
    DynamicMock coreMock;

    public Mock(Class mockedType) {
        this(new CoreMock(mockedType, CoreMock.mockNameFromClass(mockedType), new LIFOInvocationDispatcher()));
    }

    public Mock(DynamicMock coreMock) {
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
