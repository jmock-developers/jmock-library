/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import java.lang.reflect.Method;
import java.util.*;


/**
 * An object that holds information about an invocation dispatched to
 * a Mock object.
 */
public class Invocation {
    private Class declaringClass;
    private String callerName;
    private String methodName;
    private List parameterTypes;
    private Class returnType;
    private List parameterValues;

    public Invocation(Class declaringClass, String callerName, String name, Class[] parameterTypes,
                      Class returnType, Object[] parameterValues) {
        this.declaringClass = declaringClass;
        this.callerName = callerName;
        this.methodName = name;
        this.parameterTypes = Arrays.asList(parameterTypes);
        this.returnType = returnType;
        if (parameterValues == null) {
            this.parameterValues = new ArrayList(0);
        } else {
            this.parameterValues = Arrays.asList(parameterValues);
        }
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
        return Collections.unmodifiableList(parameterTypes);
    }

    public List getParameterValues() {
        return Collections.unmodifiableList(parameterValues);
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
                listHashCode(parameterTypes) ^
                returnType.hashCode() ^
                listHashCode(parameterValues);
    }

    private int listHashCode(List array) {
        int hashCode = 0;
        for (Iterator i = array.iterator(); i.hasNext();) {
            hashCode ^= i.next().hashCode();
        }
        return hashCode;
    }

    public boolean equals(Invocation call) {
        return call != null
            && methodName.equals(call.methodName)
            && parameterTypes.equals(call.parameterTypes)
            && returnType.equals(call.returnType)
            && parameterValues.equals(call.parameterValues);
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        buffer.append("Invoked: ");
        buffer.append(callerName).append(".").append(methodName);
        DynamicUtil.join(parameterValues.toArray(), buffer, "(", ")");
        return buffer.append("\n");
    }
}
