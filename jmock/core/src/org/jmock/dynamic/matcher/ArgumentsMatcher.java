/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.dynamic.matcher;

import java.util.List;

import org.jmock.Constraint;
import org.jmock.dynamic.Invocation;

public class ArgumentsMatcher
        extends StatelessInvocationMatcher 
{
    private Constraint[] constraints;

    public ArgumentsMatcher(Constraint[] constraints) {
        ArgumentsMatcher.this.constraints = (Constraint[])constraints.clone();
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

    public String toString() {
        return writeTo(new StringBuffer()).toString();
    }

    public StringBuffer writeTo(StringBuffer buffer) {
        buffer.append("( ");
        for (int i = 0; i < constraints.length; i++) {
            if (i > 0) buffer.append(", ");
            buffer.append( constraints[i] );
        }
        buffer.append(" )");
        
        return buffer;
    }
}
