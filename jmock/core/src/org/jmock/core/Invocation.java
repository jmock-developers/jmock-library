/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * A "dumb struct" that holds information about an invocation dispatched to a Mock object.
 */
public class Invocation implements SelfDescribing
{
    public final Object invokedObject;
    public final Method invokedMethod;
    public final List parameterValues;

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
        buffer.append(invokedMethod.getDeclaringClass().getName());
        buffer.append(".");
        buffer.append(invokedMethod.getName());
        Formatting.join(parameterValues, buffer, "(", ")");
        return buffer.append("\n");
    }
}
