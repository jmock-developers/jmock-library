/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.dynamic.CoreMock;
import org.jmock.dynamic.DynamicMock;
import org.jmock.dynamic.InvocationMocker;
import org.jmock.dynamic.LIFOInvocationDispatcher;
import org.jmock.dynamic.matcher.MethodNameMatcher;
import org.jmock.expectation.Verifiable;

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

    public MatchBuilder method(String methodName) {
    	InvocationMocker mocker = new InvocationMocker();
    	mocker.addMatcher( new MethodNameMatcher(methodName));
        coreMock.add(mocker);
        return new InvocationMockerBuilder(mocker);
    }
}
