package org.jmock;

import org.jmock.core.InvocationMatcher;
import org.jmock.core.MockObjectSupportTestCase;
import org.jmock.core.Stub;
import org.jmock.core.matcher.InvokeAtLeastOnceMatcher;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.matcher.TestFailureMatcher;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.ThrowStub;


public class MockObjectTestCase 
	extends MockObjectSupportTestCase
{
    public Mock mock( Class mockedType ) {
        return mock( mockedType, defaultMockNameForType(mockedType) );
    }
    
    public Mock mock( Class mockedType, String roleName ) {
        return mock(mockedType,roleName);
    }
    
    public String defaultMockNameForType( Class mockedType ) {
        String fullTypeName = mockedType.getName();
        String typeName = fullTypeName.substring( 
            Math.max(fullTypeName.lastIndexOf('.'),fullTypeName.lastIndexOf('$')) + 1 );
        
        return "mock" + typeName;
    }
    
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
    
    public InvocationMatcher never() {
        return new TestFailureMatcher("expect not called");
    }
}
