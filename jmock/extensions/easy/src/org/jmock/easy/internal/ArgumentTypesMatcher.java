/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.easy.internal;

import org.jmock.core.Constraint;
import org.jmock.core.Invocation;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.matcher.StatelessInvocationMatcher;

public class ArgumentTypesMatcher
        extends StatelessInvocationMatcher
{
    private Constraint equalTypes;

    public ArgumentTypesMatcher( Class[] argumentTypes ) {
        equalTypes = new IsEqual(argumentTypes);
    }

    public boolean matches( Invocation invocation ) {
        return equalTypes.eval(invocation.invokedMethod.getParameterTypes());
    }
    
    public StringBuffer describeTo( StringBuffer buffer ) {
        buffer.append("( ");
        equalTypes.describeTo(buffer);
        buffer.append(" )");

        return buffer;
    }
}
