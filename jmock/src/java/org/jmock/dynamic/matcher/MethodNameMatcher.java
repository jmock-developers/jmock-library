/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.matcher;

import org.jmock.Constraint;
import org.jmock.constraint.IsEqual;
import org.jmock.dynamic.framework.Invocation;

public class MethodNameMatcher
    extends StatelessInvocationMatcher 
{
    private Constraint constraint;

    public MethodNameMatcher(Constraint constraint) {
        this.constraint = constraint;
    }

    public MethodNameMatcher(String methodName) {
        this(new IsEqual(methodName));
    }

    public boolean matches(Invocation invocation) {
        return constraint.eval(invocation.getMethodName());
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        return buffer.append("Method ").append(constraint);
    }
}
