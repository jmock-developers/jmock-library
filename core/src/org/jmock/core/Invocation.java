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
	private Method method;
    private Object[] parameterValues;

	public Invocation( Object invoked, Method method, Object[] parameterValues ) {
        this.invoked = invoked;
	    this.method = method;
        this.parameterValues =
            (parameterValues == null ? new Object[0] : parameterValues);
    }

    public Object getInvokedObject() {
        return invoked;
    }
    
    public Class getDeclaringClass() {
        return method.getDeclaringClass();
    }

    public String getMethodName() {
        return method.getName();
    }

    public List getParameterTypes() {
        return Collections.unmodifiableList(Arrays.asList(method.getParameterTypes()));
    }

    public List getParameterValues() {
        return Collections.unmodifiableList(Arrays.asList(parameterValues));
    }

    public Class getReturnType() {
        return method.getReturnType();
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
            && method.equals(other.method)
            && Arrays.equals(parameterValues, other.parameterValues);
    }

    public int hashCode() {
        return invoked.hashCode() ^
               method.hashCode() ^
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
	    buffer.append(method.getDeclaringClass().getName());
	    buffer.append(".");
        buffer.append(method.getName());
        DynamicUtil.join(parameterValues, buffer, "(", ")");
        return buffer.append("\n");
    }
}
