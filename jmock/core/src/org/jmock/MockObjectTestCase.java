package org.jmock;

import org.jmock.core.InvocationMatcher;
import org.jmock.core.MockObjectSupportTestCase;
import org.jmock.core.Stub;
import org.jmock.core.matcher.InvokeAtLeastOnceMatcher;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.matcher.TestFailureMatcher;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.ThrowStub;


public abstract class MockObjectTestCase 
	extends MockObjectSupportTestCase
{
	public Stub returnValue( Object o ) {
	    return new ReturnStub(o);
    }
    
    public Stub returnValue( boolean result ) {
        return returnValue(new Boolean(result));
    }
    
    public Stub returnValue( byte result ) {
        return returnValue(new Byte(result));
    }
    
    public Stub returnValue( char result ) {
        return returnValue(new Character(result));
    }
    
    public Stub returnValue( short result ) {
        return returnValue(new Short(result));
    }
    
    public Stub returnValue( int result ) {
        return returnValue(new Integer(result));
    }
    
    public Stub returnValue( long result ) {
        return returnValue(new Long(result));
    }
    
    public Stub returnValue( float result ) {
        return returnValue(new Float(result));
    }
    
    public Stub returnValue( double result ) {
        return returnValue(new Double(result));
    }
    
    public Stub throwException( Throwable throwable ) {
        return new ThrowStub(throwable);
    }
    
    public InvocationMatcher once() {
        return new InvokeOnceMatcher();
    }
    
    public InvocationMatcher atLeastOnce() {
        return new InvokeAtLeastOnceMatcher();
    }
    
    public InvocationMatcher notCalled() {
        return new TestFailureMatcher("expect not called");
    }
}
