/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package test.jmock.dynamic.stub;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.stub.DefaultResultStub;
import org.jmock.expectation.AssertMo;


public class DefaultResultStubTest 
	extends TestCase 
{
	static final Object[] NO_ARG_VALUES = new Object[0];
	static final Class[] NO_ARG_TYPES = new Class[0];
	
	private DefaultResultStub stub;
    
	public DefaultResultStubTest(String name) {
		super(name);
	}
	
	public void setUp() {
		stub = new DefaultResultStub();
	}
	
	public void testWritesDescritionToStringBuffer() {
		AssertMo.assertIncludes( "contains expected description", 
			"guessed result", stub.writeTo(new StringBuffer()).toString() );
	}
	
	public void testIsConstructedEmpty() throws Throwable {
        assertHasNotRegisteredReturnType(stub,String.class);
        assertHasNotRegisteredReturnType(stub,int.class);
        assertHasNotRegisteredReturnType(stub,void.class);
	}
	
	public void testReturnsRegisteredValuesForAppropriateReturnTypesFromCall()
		throws Throwable
	{
		stub.addResult( String.class, "hello" );        
		stub.addResult( int.class, new Integer(0) );
		
		assertEquals( "expected registered value for string result type",
				      "hello", stub.invoke(resultCall(String.class)) );
		
		assertEquals( "expected registered value for int result type",
					  new Integer(0), stub.invoke(resultCall(int.class)) );
	}
	
	public void testRegisteredResultOverridePreviousResultsForTheSameType()
	    throws Throwable
	{
		stub.addResult( String.class, "result1" );
		stub.addResult( String.class, "result2" );
		
		assertEquals( "expected second result",
				      "result2", stub.invoke(resultCall(String.class)) );
	}
	
	public void testCallWithUnregisteredReturnTypeThrowsAssertionFailedError()
        throws Throwable
    {
        Class unsupportedReturnType = Long.class;
        Class[] supportedReturnTypes = new Class[] {
             String.class, int.class, Double.class
        };
        
		try {
            for( int i = 0; i < supportedReturnTypes.length; i++ ) {
            	stub.addResult( supportedReturnTypes[i], null ); // don't care about return value
            }
            
            stub.invoke( resultCall(unsupportedReturnType) );
		}
		catch( AssertionFailedError ex ) {
			String message = ex.getMessage();
			
            AssertMo.assertIncludes( "message should include unsupported type",
                                     unsupportedReturnType.toString(), message );
            
            for( int i = 0; i < supportedReturnTypes.length; i++ ) {
            	AssertMo.assertIncludes( "message should include expected types",
            			supportedReturnTypes[i].toString(), message );
            }
		}
	}
	
	public void testCallWhenNoRegisteredReturnTypeThrowsAssertionFailedError()
        throws Throwable
    {
		try {
            stub.invoke( resultCall(Long.class) );
		}
		catch( AssertionFailedError ex ) {
			String message = ex.getMessage();
			
			AssertMo.assertIncludes( 
				"message should report no registered return types",
				"no result types are registered", message );
		}
	}
	
	public void testFactoryMethodCreatesAStubLoadedWithUsefulResults()
		throws Throwable
	{
		stub = DefaultResultStub.createStub();
		
        assertHasRegisteredValue( stub, void.class, null );
		assertHasRegisteredValue( stub, byte.class, new Byte((byte)0) );
		assertHasRegisteredValue( stub, short.class, new Short((short)0) );
		assertHasRegisteredValue( stub, int.class, new Integer(0) );
		assertHasRegisteredValue( stub, long.class, new Long(0L) );
		assertHasRegisteredValue( stub, char.class, new Character('\0') );
		assertHasRegisteredValue( stub, float.class, new Float(0.0F) );
		assertHasRegisteredValue( stub, double.class, new Double(0.0) );
		assertHasRegisteredValue( stub, Byte.class, new Byte((byte)0) );
		assertHasRegisteredValue( stub, Short.class, new Short((short)0) );
		assertHasRegisteredValue( stub, Integer.class, new Integer(0) );
		assertHasRegisteredValue( stub, Long.class, new Long(0L) );
		assertHasRegisteredValue( stub, Character.class, new Character('\0') );
		assertHasRegisteredValue( stub, Float.class, new Float(0.0F) );
		assertHasRegisteredValue( stub, Double.class, new Double(0.0) );
		assertHasRegisteredValue( stub, String.class, "<default string result>" );
	}
	
	public void assertHasRegisteredValue( DefaultResultStub defaultResultStub,
										  Class resultType, 
										  Object resultValue )
		throws Throwable
	{
		assertEquals( "expected "+resultValue+" to be returned",
					  resultValue, defaultResultStub.invoke(resultCall(resultType)) );
	}
	
    public void assertHasNotRegisteredReturnType( DefaultResultStub defaultResultStub,
												  Class resultType )
        throws Throwable
    {
    	try {
            defaultResultStub.invoke(resultCall(resultType));
            fail("stub should not support return type " + resultType);
        }
        catch( AssertionFailedError expected ) {
        	return;
        }
    }
    
	private Invocation resultCall( Class resultType ) {
		return new Invocation(
			getClass(), "ignoredMethodName", NO_ARG_TYPES, resultType, NO_ARG_VALUES );
	}
}
