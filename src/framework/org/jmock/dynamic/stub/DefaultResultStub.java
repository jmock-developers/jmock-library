/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.stub;

import java.lang.reflect.Array;
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
	
    public DefaultResultStub() {
        createDefaultResults();
    }
    
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
		} else if( returnType.isArray() ) {
            return Array.newInstance( returnType.getComponentType(), 0 );
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
	
	protected void createDefaultResults() {
        addResult( void.class,        null );
		addResult( byte.class,        new Byte((byte)0) );
		addResult( short.class,       new Short((short)0) );
		addResult( int.class,         new Integer(0) );
		addResult( long.class,        new Long(0L) );
		addResult( char.class,        new Character('\0') );
		addResult( float.class,       new Float(0.0F) );
		addResult( double.class,      new Double(0.0) );
		addResult( Byte.class,        new Byte((byte)0) );
		addResult( Short.class,       new Short((short)0) );
		addResult( Integer.class,     new Integer(0) );
		addResult( Long.class,        new Long(0L) );
		addResult( Character.class,   new Character('\0') );
		addResult( Float.class,       new Float(0.0F) );
		addResult( Double.class,      new Double(0.0) );
		addResult( String.class,      "<default string result>" );
	}
}
