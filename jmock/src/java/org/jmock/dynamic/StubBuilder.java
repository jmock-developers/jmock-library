/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;


public interface StubBuilder {
    ExpectationBuilder isVoid();

    ExpectationBuilder returns(Object returnValue);

    ExpectationBuilder willThrow(Throwable throwable);
}

