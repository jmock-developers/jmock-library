package org.jmock.builder;

import org.jmock.dynamic.Stub;
import org.jmock.dynamic.stub.ReturnStub;
import org.jmock.dynamic.stub.ThrowStub;
import org.jmock.util.MockObjectSupportTestCase;


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
}
