/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;



public interface StubBuilder extends ExpectationBuilder {
    ExpectationBuilder isVoid();
    ExpectationBuilder willReturn(Object returnValue);
    ExpectationBuilder willThrow(Throwable throwable);
}

