/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import org.jmock.expectation.Verifiable;

import java.lang.reflect.InvocationHandler;

public interface DynamicMock
        extends Verifiable, InvocationHandler {
    void add(Invokable invokable);

    Object proxy();

    void reset();
}