/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.matcher;

import org.jmock.Constraint;
import org.jmock.dynamic.framework.Invocation;

import java.util.List;

public class ArgumentsMatcher
        extends StatelessInvocationMatcher 
{
    private Constraint[] constraints;

    // TODO: encapsulate the constraints array
    public ArgumentsMatcher(Constraint[] constraints) {
        ArgumentsMatcher.this.constraints = constraints;
    }

    public boolean matches(Invocation invocation) {
        return constraints.length == invocation.getParameterValues().size()
                && matchesValues(invocation.getParameterValues());
    }

    private boolean matchesValues(List list) {
        for (int i = 0; i < constraints.length; ++i) {
            if (!constraints[i].eval(list.get(i))) {
                return false;
            }
        }
        return true;
    }

    // TODO: encapsulate the constraints array
    public Constraint[] getConstraints() {
        return constraints;
    }

    public String toString() {
        return writeTo(new StringBuffer()).toString();
    }

    public boolean equals(Object other) {
        return other != null && toString().equals(other.toString());
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        buffer.append("Arguments [");
        for (int i = 0; i < constraints.length; ++i) {
            buffer.append(constraints[i]).append(", ");
        }
        buffer.append("]");
        return buffer;
    }
}