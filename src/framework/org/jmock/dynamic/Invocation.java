/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An object that holds information about an invocation dispatched to
 * a Mock object.
 */
public class Invocation {
    private Class declaringClass;
    private String callerName;
    private String methodName;
    private Class[] parameterTypes;
    private Class returnType;
    private Object[] parameterValues;

    public Invocation(Class declaringClass, String callerName, String name, Class[] parameterTypes,
                      Class returnType, Object[] parameterValues) {
        this.declaringClass = declaringClass;
        this.callerName = callerName;
        this.methodName = name;
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
        this.parameterValues = 
            (parameterValues == null ? new Object[0] : parameterValues);
    }

    public Invocation(Method method, String callerName, Object[] parameterValues) {
        this(method.getDeclaringClass(), callerName, method.getName(), method.getParameterTypes(),
                method.getReturnType(), parameterValues);
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
    
    public boolean equals(Invocation call) {
        return call != null
            && methodName.equals(call.methodName)
            && Arrays.equals(parameterTypes, call.parameterTypes)
            && returnType.equals(call.returnType)
            && Arrays.equals(parameterValues, call.parameterValues);
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        buffer.append("Invoked: ");
        buffer.append(callerName).append(".").append(methodName);
        DynamicUtil.join(parameterValues, buffer, "(", ")");
        return buffer.append("\n");
    }
}
