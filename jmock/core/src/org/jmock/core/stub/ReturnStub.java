/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.stub;

import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.jmock.core.Invocation;
import org.jmock.core.Stub;


public class ReturnStub
        extends Assert
        implements Stub
{
    private Object result;

    private static Map BOX_TYPES = new HashMap();

    static {
        BOX_TYPES.put(boolean.class, Boolean.class);
        BOX_TYPES.put(byte.class, Byte.class);
        BOX_TYPES.put(char.class, Character.class);
        BOX_TYPES.put(short.class, Short.class);
        BOX_TYPES.put(int.class, Integer.class);
        BOX_TYPES.put(long.class, Long.class);
        BOX_TYPES.put(float.class, Float.class);
        BOX_TYPES.put(double.class, Double.class);
    }


    public ReturnStub( Object result ) {
        this.result = result;
    }

    public Object invoke( Invocation invocation ) throws Throwable {
        checkTypeCompatiblity(invocation.invokedMethod.getReturnType());
        return result;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("returns <").append(result).append(">");
    }

    private void checkTypeCompatiblity( Class returnType ) {
        if (result == null) {
            if (returnType.isPrimitive()) reportInvalidNullValue(returnType);
        } else if (returnType == void.class) {
            reportReturnFromVoidMethod();
        } else {
            Class valueType = result.getClass();
            if (!isCompatible(returnType, valueType)) {
                reportTypeError(returnType, valueType);
            }
        }
    }

    private boolean isCompatible( Class returnType, Class valueType ) {
        if (returnType.isPrimitive()) {
            return isBoxedType(returnType, valueType);
        } else {
            return returnType.isAssignableFrom(valueType);
        }
    }

    private boolean isBoxedType( Class primitiveType, Class referenceType ) {
        return BOX_TYPES.get(primitiveType) == referenceType;
    }

    private void reportTypeError( Class returnType, Class valueType ) {
        fail("tried to return an incompatible value: " +
             "expected a " + returnType + " but returned a " + valueType);
    }

    private void reportInvalidNullValue( Class returnType ) {
        fail("tried to return null value from invokedMethod returning " + returnType);
    }

    private void reportReturnFromVoidMethod() {
        fail("tried to return a value from a void method");
    }
}
