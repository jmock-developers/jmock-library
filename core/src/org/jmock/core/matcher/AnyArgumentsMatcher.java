/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.core.matcher;

import org.jmock.core.Invocation;


public class AnyArgumentsMatcher extends StatelessInvocationMatcher
{

    public static final AnyArgumentsMatcher INSTANCE = new AnyArgumentsMatcher();

    public boolean matches( Invocation invocation ) {
        return true;
    }

    public StringBuffer describeTo( StringBuffer buffer ) {
        return buffer.append("(any arguments)");
    }
}