/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core;

import java.lang.reflect.Method;
import java.util.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

/**
 * The static details about a method and the run-time details of its invocation.
 * @since 1.0
 */
public class Invocation implements SelfDescribing
{
    public final Object invokedObject;
    public final Method invokedMethod;
    public final List parameterValues;

    // Yuck, but there doesn't seem to be a better way.
    private static final Map BOX_TYPES = makeBoxTypesMap();
    
    public Invocation( Object invoked, Method method, Object[] parameterValues ) {
        this.invokedObject = invoked;
        this.invokedMethod = method;
        this.parameterValues = parameterValues == null ?
                               Collections.EMPTY_LIST
                               : Collections.unmodifiableList(Arrays.asList(parameterValues));
    }

    public String toString() {
        return describeTo(new StringBuffer()).toString();
    }

    public boolean equals( Object other ) {
        return (other instanceof Invocation) && this.equals((Invocation)other);
    }

    public boolean equals( Invocation other ) {
        return other != null
               && invokedObject == other.invokedObject
               && invokedMethod.equals(other.invokedMethod)
               && parameterValues.equals(other.parameterValues);
    }

    public int hashCode() {
        return invokedObject.hashCode() ^
               invokedMethod.hashCode() ^
               parameterValues.hashCode();
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        buffer.append(invokedObject.toString()).append(".").append(invokedMethod.getName());
        Formatting.join(parameterValues, buffer, "(", ")");
        return buffer;
    }

    public void checkReturnTypeCompatibility(final Object result) {
        Class returnType = invokedMethod.getReturnType();
        if (returnType == void.class) {
            failIfReturnTypeIsNotNull(result);
        } else if (result == null) {
            failIfReturnTypeIsPrimitive();
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
        } 
        return returnType.isAssignableFrom(valueType);
    }

    private boolean isBoxedType( Class primitiveType, Class referenceType ) {
        return BOX_TYPES.get(primitiveType) == referenceType;
    }

    private void failIfReturnTypeIsNotNull(final Object result) {
        Assert.assertNull("tried to return a value from a void method", result);
    }

    private void failIfReturnTypeIsPrimitive() {
        Class returnType = invokedMethod.getReturnType();
        Assert.assertFalse(
                "tried to return null value from method returning " + returnType.getName(),
                returnType.isPrimitive());
    }

    private void reportTypeError( Class returnType, Class valueType ) {
        Assert.fail("tried to return an incompatible value: " +
             "expected a " + returnType.getName() + " but returned a " + valueType.getName());
    }

    private static Map makeBoxTypesMap() {
        HashMap map = new HashMap();
        map.put(boolean.class, Boolean.class);
        map.put(byte.class, Byte.class);
        map.put(char.class, Character.class);
        map.put(short.class, Short.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(float.class, Float.class);
        map.put(double.class, Double.class);
        return map;
    }
}
