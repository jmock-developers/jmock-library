/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.stub;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.AssertionFailedError;

import org.jmock.dynamic.Invocation;
import org.jmock.dynamic.Stub;

public class DefaultResultStub
	implements Stub
{
	private Map resultValuesByType = new HashMap();
	
	public StringBuffer writeTo( StringBuffer buf ) {
		return buf.append("a guessed result");
	}
	
	public void addResult( Class resultType, Object resultValue ) {
		resultValuesByType.put( resultType, resultValue );
	}
	
	public Object invoke( Invocation invocation ) 
		throws Throwable
	{
		Class returnType = invocation.getReturnType();
        
        if( resultValuesByType.containsKey(returnType) ) {
			return resultValuesByType.get(returnType);
		} else {
			throw new AssertionFailedError( createErrorMessage(invocation) );
		}
	}
	
	public String createErrorMessage(Invocation call) {
		StringBuffer buf = new StringBuffer();
		
		buf.append("unexpected result type: ");
		buf.append(call.getReturnType().toString());
		buf.append("\n");
		
		
		if( resultValuesByType.isEmpty() ) {
			buf.append("no result types are registered!");
		} else {
			buf.append("expected one of: ");
			
			Iterator i = resultValuesByType.keySet().iterator(); 
			buf.append( i.next().toString() );
			while( i.hasNext() ) {
				buf.append(", ");
				buf.append( i.next().toString() );
			}
		}
		
		return buf.toString();
	}
	
	public static DefaultResultStub createStub() {
		DefaultResultStub stub = new DefaultResultStub();
		
        stub.addResult( void.class, null );
		stub.addResult( byte.class, new Byte((byte)0) );
		stub.addResult( short.class, new Short((short)0) );
		stub.addResult( int.class, new Integer(0) );
		stub.addResult( long.class, new Long(0L) );
		stub.addResult( char.class, new Character('\0') );
		stub.addResult( float.class, new Float(0.0F) );
		stub.addResult( double.class, new Double(0.0) );
		stub.addResult( Byte.class, new Byte((byte)0) );
		stub.addResult( Short.class, new Short((short)0) );
		stub.addResult( Integer.class, new Integer(0) );
		stub.addResult( Long.class, new Long(0L) );
		stub.addResult( Character.class, new Character('\0') );
		stub.addResult( Float.class, new Float(0.0F) );
		stub.addResult( Double.class, new Double(0.0) );
		stub.addResult( String.class, "<default string result>" );
		
		return stub;
	}
}
