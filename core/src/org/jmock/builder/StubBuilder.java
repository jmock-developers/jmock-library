/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.builder;

import org.jmock.dynamic.Stub;



public interface StubBuilder extends IdentityBuilder {
    IdentityBuilder will( Stub stubAction );
    IdentityBuilder isVoid();
    
    /**
     * @deprecated use will(customStub).  Will be removed in version 1.0.
     */
	IdentityBuilder stub( Stub customStub );
	
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    IdentityBuilder willReturn(Object result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    IdentityBuilder willReturn(boolean result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    IdentityBuilder willReturn(char result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    IdentityBuilder willReturn(short result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    IdentityBuilder willReturn(byte result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    IdentityBuilder willReturn(int result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    IdentityBuilder willReturn(long result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    IdentityBuilder willReturn(float result);
    
    /**
     * @deprecated use will(returnValue(result)).  Will be removed in version 1.0.
     */
    IdentityBuilder willReturn(double result);
    
    /**
     * @deprecated use will(throwException(result)).  Will be removed in version 1.0.
     */
    IdentityBuilder willThrow(Throwable throwable);
}

