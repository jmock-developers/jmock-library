/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.matcher;

import org.jmock.core.Invocation;


public class NoArgumentsMatcher extends StatelessInvocationMatcher
{
    public static final NoArgumentsMatcher INSTANCE = new NoArgumentsMatcher();

    public boolean matches( Invocation invocation ) {
        return invocation.parameterValues.isEmpty();
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("(no arguments)");
    }
}
