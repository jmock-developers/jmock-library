package org.jmock.dynamic.stub;

import junit.framework.AssertionFailedError;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.Stub;


public class TestFailureStub implements Stub {
    String errorMessage;
    
    public TestFailureStub( String errorMessage ) {
        this.errorMessage = errorMessage;
    }
    
    public Object invoke( Invocation invocation ) throws Throwable {
		throw new AssertionFailedError(errorMessage);
    }
    
    public StringBuffer describeTo(StringBuffer buffer) {
        return buffer.append("fails the test and reports \"" + errorMessage + "\"");
    }
}
