/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An object that holds information about an invocation dispatched to
 * a Mock object.
 */
public class Invocation {
    private Object invoked;
    private Class declaringClass;
    private String methodName;
    private Class[] parameterTypes;
    private Class returnType;
    private Object[] parameterValues;

    private Invocation( Object invoked, Class declaringClass,
                       String name, Class[] parameterTypes, Class returnType,
                       Object[] parameterValues ) 
    {
        this.invoked = invoked;
        this.declaringClass = declaringClass;
        this.methodName = name;
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
        this.parameterValues = 
            (parameterValues == null ? new Object[0] : parameterValues);
    }
	
    public Invocation( Object invoked, Method method, Object[] parameterValues ) {
        this( invoked, method.getDeclaringClass(), 
              method.getName(), method.getParameterTypes(), method.getReturnType(),
              parameterValues );
    }

    public Object getInvokedObject() {
        return invoked;
    }
    
    public Class getDeclaringClass() {
        return declaringClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public List getParameterTypes() {
        return Collections.unmodifiableList(Arrays.asList(parameterTypes));
    }

    public List getParameterValues() {
        return Collections.unmodifiableList(Arrays.asList(parameterValues));
    }

    public Class getReturnType() {
        return returnType;
    }

    public String toString() {
        return writeTo(new StringBuffer()).toString();
    }

    public boolean equals(Object other) {
        return (other instanceof Invocation) && this.equals((Invocation) other);
    }

    public boolean equals(Invocation other) {
        return other != null
            && invoked == other.invoked
            && methodName.equals(other.methodName)
            && Arrays.equals(parameterTypes, other.parameterTypes)
            && returnType.equals(other.returnType)
            && Arrays.equals(parameterValues, other.parameterValues);
    }

    public int hashCode() {
        return methodName.hashCode() ^
                arrayHashCode(parameterTypes) ^
                returnType.hashCode() ^
                arrayHashCode(parameterValues);
    }

    private int arrayHashCode(Object[] array) {
        int hashCode = 0;
        for (int i = 0; i < array.length; ++i) {
            hashCode ^= array[i].hashCode();
        }
        return hashCode;
    }
    
    public StringBuffer writeTo(StringBuffer buffer) {
        buffer.append(methodName);
        DynamicUtil.join(parameterValues, buffer, "(", ")");
        return buffer.append("\n");
    }
}
