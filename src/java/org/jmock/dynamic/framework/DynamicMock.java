/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.framework;

import org.jmock.expectation.Verifiable;

import java.lang.reflect.InvocationHandler;

public interface DynamicMock
        extends Verifiable, InvocationHandler 
{
    Object proxy();

    void add(Invokable invokable);

    void reset();
}

