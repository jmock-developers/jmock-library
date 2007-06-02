/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.matcher;

import java.util.List;
import org.jmock.core.Constraint;
import org.jmock.core.Invocation;


public class ArgumentsMatcher
        extends StatelessInvocationMatcher
{
    private Constraint[] constraints;

    public ArgumentsMatcher( Constraint[] constraints ) {
        ArgumentsMatcher.this.constraints = (Constraint[])constraints.clone();
    }

    public boolean matches( Invocation invocation ) {
        return constraints.length == invocation.parameterValues.size()
               && matchesValues(invocation.parameterValues);
    }

    private boolean matchesValues( List list ) {
        for (int i = 0; i < constraints.length; ++i) {
            if (!constraints[i].eval(list.get(i))) {
                return false;
            }
        }
        return true;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        buffer.append("( ");
        for (int i = 0; i < constraints.length; i++) {
            if (i > 0) buffer.append(", ");
            constraints[i].describeTo(buffer);
        }
        buffer.append(" )");

        return buffer;
    }
}
