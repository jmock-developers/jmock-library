package org.jmock.core.matcher;

import org.jmock.core.Invocation;

import java.util.List;


public class NoArgumentsMatcher extends StatelessInvocationMatcher {
    public static final NoArgumentsMatcher INSTANCE = new NoArgumentsMatcher();
    
    public boolean matches(Invocation invocation) {
	    return invocation.parameterValues.isEmpty();
    }
    public StringBuffer describeTo(StringBuffer buffer) {
        return buffer.append("(no arguments)");
    }
}