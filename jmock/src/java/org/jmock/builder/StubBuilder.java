/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.dynamic.Stub;



public interface StubBuilder extends ExpectationBuilder {
	ExpectationBuilder stub( Stub customStub );
	
    ExpectationBuilder isVoid();
    
    ExpectationBuilder willReturn(Object returnValue);
    ExpectationBuilder willReturn(boolean booleanValue);
    ExpectationBuilder willReturn(char charValue);
    ExpectationBuilder willReturn(short shortValue);
    ExpectationBuilder willReturn(byte byteValue);
    ExpectationBuilder willReturn(int returnValue);
    ExpectationBuilder willReturn(long longValue);
    ExpectationBuilder willReturn(float floatValue);
    ExpectationBuilder willReturn(double doubleValue);
    
    ExpectationBuilder willThrow(Throwable throwable);
}

