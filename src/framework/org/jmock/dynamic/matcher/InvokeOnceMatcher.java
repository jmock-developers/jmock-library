/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.matcher;

import org.jmock.dynamic.Invocation;

public class InvokeOnceMatcher
	extends InvokedRecorder
{
    public boolean matches(Invocation invocation) {
        return !hasBeenInvoked();
    }
    
    public void verify() {
    	verifyHasBeenInvoked();
    }
    
    public StringBuffer writeTo(StringBuffer buffer) {
        buffer.append("expected once");
        if( hasBeenInvoked() ) buffer.append(" and has been invoked");
        return buffer;
    }
}
