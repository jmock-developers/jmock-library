/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core.stub;

import org.jmock.core.Invocation;
import org.jmock.core.Stub;
import junit.framework.AssertionFailedError;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.HashMap;

public class ReturnStub
    implements Stub 
{
    private Object result;

	private static Map BOX_TYPES = new HashMap();
	static {
		BOX_TYPES.put( boolean.class, Boolean.class );
		BOX_TYPES.put( byte.class, Byte.class );
		BOX_TYPES.put( char.class, Character.class );
		BOX_TYPES.put( short.class, Short.class );
		BOX_TYPES.put( int.class, Integer.class );
		BOX_TYPES.put( long.class, Long.class );
		BOX_TYPES.put( float.class, Float.class );
		BOX_TYPES.put( double.class, Double.class );
	}


    public ReturnStub(Object result) {
        this.result = result;
    }

    public Object invoke(Invocation invocation) throws Throwable {
	    checkTypeCompatiblity( invocation.getReturnType() );
	    return result;
    }

	public StringBuffer describeTo(StringBuffer buffer) {
        return buffer.append("returns <").append(result).append(">");
    }

	private void checkTypeCompatiblity(Class returnType) {
		if( !isCompatible( returnType, result.getClass() )  ) {
			throw new AssertionFailedError( "result value has wrong type: " +
			                                "expected a " + returnType +
			                                " but returned a " + result.getClass() );
		}
	}

	private boolean isCompatible( Class returnType, Class valueType ) {
		if( returnType.isPrimitive() ) {
			return isBoxedType( returnType, valueType );
		} else {
			return returnType.isAssignableFrom(valueType);
		}
	}

	private boolean isBoxedType( Class primitiveType, Class referenceType ) {
		return BOX_TYPES.get(primitiveType) == referenceType;
	}
}
