/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.dynamic.InvocationMatcher;
import org.jmock.dynamic.Stub;



public interface StubBuilder extends ExpectationBuilder {
    StubBuilder match( InvocationMatcher customMatcher );
    
    ExpectationBuilder will( Stub stubAction );
    ExpectationBuilder isVoid();
    
    /**
     * @deprecated use will(customStub).  Will be removed in version 1.0.
     */
	ExpectationBuilder stub( Stub customStub );
	
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    ExpectationBuilder willReturn(Object result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    ExpectationBuilder willReturn(boolean result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    ExpectationBuilder willReturn(char result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    ExpectationBuilder willReturn(short result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    ExpectationBuilder willReturn(byte result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    ExpectationBuilder willReturn(int result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    ExpectationBuilder willReturn(long result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    ExpectationBuilder willReturn(float result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    ExpectationBuilder willReturn(double result);
    
    /**
     * @deprecated use will(throwException(result)).  Will be removed in version 1.0.
     */
    ExpectationBuilder willThrow(Throwable throwable);
}

