/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;


/**
 * An object that holds information about a invocation dispatched to
 * a Mock object.
 */
public class Invocation {
    private Class declaringClass;
    private String name;
    private List parameterTypes;
    private Class returnType;
    private List parameterValues;

    public Invocation(Class declaringClass, String name, Class[] parameterTypes,
                      Class returnType, Object[] parameterValues) {
        this.declaringClass = declaringClass;
        this.name = name;
        this.parameterTypes = Arrays.asList(parameterTypes);
        this.returnType = returnType;
        if (parameterValues == null) {
            this.parameterValues = new ArrayList(0);
        } else {
            this.parameterValues = Arrays.asList(parameterValues);
        }
    }

    public Invocation(Method method, Object[] parameterValues) {
        this(method.getDeclaringClass(), method.getName(), method.getParameterTypes(),
                method.getReturnType(), parameterValues);
    }

    public String getMethodName() {
        return name;
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
        return DynamicUtil.methodToString(name, parameterValues.toArray());
    }

    public boolean equals(Object other) {
        return (other instanceof Invocation) && this.equals((Invocation) other);
    }

    public int hashCode() {
        return name.hashCode() ^
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
            && name.equals(call.name)
            && parameterTypes.equals(call.parameterTypes)
            && returnType.equals(call.returnType)
            && parameterValues.equals(call.parameterValues);
    }

    boolean isCheckingEqualityOnProxy() {
        return name.equals("equals")
                && parameterValues.size() == 1
                && parameterValues.get(0) != null
                && Proxy.isProxyClass(parameterValues.get(0).getClass());
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        buffer.append("Invocation: ");
        writeDeclaringClassName(buffer).append(".")
                .append(name).append("(");
        DynamicUtil.join(parameterValues.toArray(), buffer);
        buffer.append(")\n");
        return buffer;
    }
    
    private StringBuffer writeDeclaringClassName(StringBuffer buffer) {
        return buffer.append(declaringClass.getName().substring(declaringClass.getPackage().getName().length()));
    }
}
