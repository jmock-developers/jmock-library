/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core.stub;

import org.jmock.core.Invocation;
import org.jmock.core.Stub;


public class VoidStub
        implements Stub
{
	public static final VoidStub INSTANCE = new VoidStub();

	public Object invoke( Invocation invocation ) throws Throwable {
		return null;
	}

	public StringBuffer describeTo( StringBuffer buffer ) {
		return buffer.append("is void");
	}
}
