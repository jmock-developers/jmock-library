/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.stub;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.Stub;

public class VoidStub 
	implements Stub
{
	public static final VoidStub INSTANCE = new VoidStub();
	
    public Object invoke(Invocation invocation) throws Throwable {
        return null;
    }
    
    public StringBuffer writeTo(StringBuffer buffer) {
        return buffer.append("returns <void>");
    }
}
